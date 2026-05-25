"use client";

import React from 'react';
import { Product } from '../types';
import { useCart } from '../context/CartContext';
import { Button } from './ui/button';
import { ShoppingCart } from 'lucide-react';
import toast from 'react-hot-toast';

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  const { addToCart } = useCart();

  const handleAddToCart = () => {
    addToCart(product);
    toast.success(`${product.name} added to cart`);
  };

  const isOutOfStock = product.quantity <= 0;

  return (
    <div className="group relative flex flex-col overflow-hidden rounded-xl bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 shadow-sm hover:shadow-md transition-all duration-300">
      <div className="aspect-[4/3] w-full bg-gray-100 dark:bg-gray-800 overflow-hidden relative">
        <div className="absolute inset-0 flex items-center justify-center text-gray-400 dark:text-gray-500">
          {/* Placeholder for Product Image */}
          <span className="text-sm font-medium uppercase tracking-widest">{product.name.substring(0, 2)}</span>
        </div>
        {isOutOfStock && (
          <div className="absolute top-2 right-2 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded-md z-10">
            Out of Stock
          </div>
        )}
      </div>
      
      <div className="flex flex-1 flex-col p-5">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white line-clamp-1">{product.name}</h3>
          <p className="mt-2 text-sm text-gray-500 dark:text-gray-400 line-clamp-2">{product.description}</p>
        </div>
        
        <div className="mt-4 flex items-center justify-between">
          <span className="text-xl font-bold text-gray-900 dark:text-white">${product.price.toFixed(2)}</span>
          <span className="text-xs text-gray-500 dark:text-gray-400">Stock: {product.quantity}</span>
        </div>

        <Button 
          className="mt-4 w-full gap-2" 
          disabled={isOutOfStock}
          onClick={handleAddToCart}
        >
          <ShoppingCart className="w-4 h-4" />
          {isOutOfStock ? 'Sold Out' : 'Add to Cart'}
        </Button>
      </div>
    </div>
  );
}
