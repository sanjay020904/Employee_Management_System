import React from 'react';

export const metadata = {
  title: 'Offline Document AI',
  description: 'Download the Offline Document AI SDK',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
