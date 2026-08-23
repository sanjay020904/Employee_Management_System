import { generateText } from 'ai';
import { createGroq } from '@ai-sdk/groq';

// Initialize Groq provider using the Vercel environment variable GROQ_API_KEY
const groq = createGroq({
  apiKey: process.env.GROQ_API_KEY || '',
});

export const maxDuration = 30; // Max timeout for Vercel Hobby

export async function POST(req: Request) {
  try {
    const { query, context, model_scale } = await req.json();

    if (!query) {
      return new Response(JSON.stringify({ error: 'Query is required' }), { status: 400 });
    }

    const prompt = `
You are a helpful AI assistant. Answer the user's question using ONLY the provided document context below. 
If the answer is not in the context, clearly state that you do not know.

Context:
${context || 'No context provided.'}

Question: ${query}
Answer:`;

    // Select model based on user's hardware scaling profile
    const modelId = model_scale === '70b' ? 'llama3-70b-8192' : 'llama3-8b-8192';

    // Generate the answer using Groq's fast Llama 3 models
    const { text } = await generateText({
      model: groq(modelId),
      prompt: prompt,
    });

    return new Response(JSON.stringify({ answer: text }), {
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (error: any) {
    console.error("API Error:", error);
    return new Response(JSON.stringify({ error: error.message || 'An error occurred' }), { status: 500 });
  }
}
