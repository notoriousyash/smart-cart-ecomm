"use client";

import { useEffect, useState } from "react";
import { ProductCard } from "../components/ProductCard";
import { Product } from "../types";
import { fetchApi } from "../utils/api";
import { Loader2 } from "lucide-react";

export default function Home() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadProducts = async () => {
      try {
        setLoading(true);
        // Using sample data if backend fails, or we can just show error
        const data = await fetchApi("/products");
        setProducts(Array.isArray(data) ? data : []);
      } catch (err: any) {
        console.error("Failed to fetch products:", err);
        setError("Failed to load products. Please ensure the backend is running.");
        
        // Fallback mock data for visual demonstration
        setProducts([
          { id: 1, name: "Premium Wireless Headphones", description: "High-fidelity audio with active noise cancellation and 30-hour battery life.", price: 299.99, quantity: 15 },
          { id: 2, name: "Mechanical Keyboard", description: "RGB backlit mechanical keyboard with tactile switches for typing and gaming.", price: 149.50, quantity: 8 },
          { id: 3, name: "Ergonomic Mouse", description: "Wireless ergonomic mouse designed for comfort during long working sessions.", price: 79.99, quantity: 0 },
          { id: 4, name: "4K Monitor", description: "27-inch 4K UHD monitor with IPS panel and ultra-thin bezels.", price: 349.00, quantity: 5 },
          { id: 5, name: "USB-C Docking Station", description: "10-in-1 hub with HDMI, ethernet, card readers, and power delivery.", price: 89.99, quantity: 20 },
          { id: 6, name: "Laptop Stand", description: "Adjustable aluminum laptop stand for better posture and cooling.", price: 45.00, quantity: 12 },
        ]);
      } finally {
        setLoading(false);
      }
    };

    loadProducts();
  }, []);

  if (loading) {
    return (
      <div className="flex-1 flex items-center justify-center min-h-[50vh]">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-in fade-in duration-500">
      <section className="text-center space-y-4 py-8">
        <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight text-gray-900 dark:text-white">
          Discover Premium Tech
        </h1>
        <p className="text-lg text-gray-600 dark:text-gray-400 max-w-2xl mx-auto">
          Upgrade your workspace with our curated selection of high-quality peripherals and accessories.
        </p>
      </section>

      {error && (
        <div className="bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800 text-amber-800 dark:text-amber-300 p-4 rounded-md text-center text-sm">
          {error} (Showing mock data for demonstration)
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
}
