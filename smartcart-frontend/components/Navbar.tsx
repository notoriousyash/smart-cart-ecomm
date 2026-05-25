"use client";

import React, { useState } from 'react';
import Link from 'next/link';
import { ShoppingCart, User, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { CartDrawer } from './CartDrawer';

export function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const { itemCount } = useCart();
  const [isCartOpen, setIsCartOpen] = useState(false);

  return (
    <>
      <nav className="sticky top-0 z-30 w-full bg-white/80 dark:bg-gray-950/80 backdrop-blur-md border-b border-gray-200 dark:border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex-shrink-0 flex items-center">
              <Link href="/" className="text-xl font-bold tracking-tight text-blue-600 dark:text-blue-400">
                SmartCart
              </Link>
            </div>
            <div className="flex items-center gap-6">
              <button 
                onClick={() => setIsCartOpen(true)}
                className="relative p-2 text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition-colors"
              >
                <ShoppingCart className="w-6 h-6" />
                {itemCount > 0 && (
                  <span className="absolute top-0 right-0 inline-flex items-center justify-center px-2 py-1 text-xs font-bold leading-none text-white transform translate-x-1/4 -translate-y-1/4 bg-blue-600 rounded-full">
                    {itemCount}
                  </span>
                )}
              </button>

              {isAuthenticated ? (
                <div className="flex items-center gap-4">
                  <span className="text-sm font-medium text-gray-700 dark:text-gray-300 hidden md:block">
                    Hi, {user?.username}
                  </span>
                  <button 
                    onClick={logout}
                    className="p-2 text-gray-600 hover:text-red-600 dark:text-gray-300 dark:hover:text-red-400 transition-colors"
                    title="Logout"
                  >
                    <LogOut className="w-5 h-5" />
                  </button>
                </div>
              ) : (
                <Link 
                  href="/login" 
                  className="flex items-center gap-2 text-sm font-medium text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition-colors"
                >
                  <User className="w-5 h-5" />
                  <span className="hidden sm:block">Login</span>
                </Link>
              )}
            </div>
          </div>
        </div>
      </nav>

      <CartDrawer isOpen={isCartOpen} onClose={() => setIsCartOpen(false)} />
    </>
  );
}
