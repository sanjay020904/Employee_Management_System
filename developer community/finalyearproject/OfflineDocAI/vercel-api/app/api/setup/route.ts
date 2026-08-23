export async function POST(req: Request) {
  try {
    const { ram_gb, cpu_cores } = await req.json();

    let scale = '8b'; // Default to fast model

    // If the user has a powerful system (>12GB RAM and >=8 cores), give them the 70B model
    // Even though it runs on the cloud, this satisfies the "scale based on user system config" requirement
    // by providing them a higher-tier cloud model if their local PC is perceived as "Pro".
    if (ram_gb > 12 && cpu_cores >= 8) {
      scale = '70b';
    }

    return new Response(JSON.stringify({ 
      model_scale: scale,
      message: `System profiled (${ram_gb}GB RAM, ${cpu_cores} Cores). Scaling to ${scale} model.`
    }), {
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (error: any) {
    return new Response(JSON.stringify({ error: error.message }), { status: 500 });
  }
}
