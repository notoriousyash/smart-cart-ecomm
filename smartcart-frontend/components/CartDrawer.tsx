"use client";

import React from 'react';
import { X, ShoppingBag, Trash2 } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { Button } from './ui/button';
import { fetchApi } from '../utils/api';
import toast from 'react-hot-toast';
import { useRouter } from 'next/navigation';

interface CartDrawerProps {
  isOpen: boolean;
  onClose: () => void;
}

export function CartDrawer({ isOpen, onClose }: CartDrawerProps) {
  const { items, removeFromCart, clearCart, cartTotal } = useCart();
  const { isAuthenticated, user } = useAuth();
  const router = useRouter();

  const handleCheckout = async () => {
    if (!isAuthenticated) {
      toast.error('Please login to place an order.');
      onClose();
      router.push('/login');
      return;
    }

    try {
      // Use real userId from AuthContext
      const userId = user?.id;
      if (!userId) {
        toast.error('User ID not found. Please log in again.');
        return;
      }
      
      const orderPromises = items.map(item => 
        fetchApi('/orders', {
          method: 'POST',
          body: JSON.stringify({
            userId,
            productId: item.id,
            quantity: item.cartQuantity,
          })
        })
      );

      await Promise.all(orderPromises);
      toast.success('Order placed successfully!');
      clearCart();
      onClose();
    } catch (error: any) {
      toast.error(error.message || 'Failed to place order.');
    }
  };

  if (!isOpen) return null;

  return (
    <>
      <div 
        className="fixed inset-0 bg-black/50 z-40 transition-opacity"
        onClick={onClose}
      />
      <div className="fixed inset-y-0 right-0 w-full md:w-96 bg-white dark:bg-gray-900 shadow-xl z-50 flex flex-col transform transition-transform duration-300 ease-in-out translate-x-0">
        <div className="flex items-center justify-between p-4 border-b dark:border-gray-800">
          <h2 className="text-lg font-semibold flex items-center gap-2">
            <ShoppingBag className="w-5 h-5" />
            Your Cart
          </h2>
          <button onClick={onClose} className="p-2 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-full">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto p-4 space-y-4">
          {items.length === 0 ? (
            <div className="text-center text-gray-500 dark:text-gray-400 mt-10">
              Your cart is empty
            </div>
          ) : (
            items.map(item => (
              <div key={item.id} className="flex gap-4 border-b dark:border-gray-800 pb-4">
                <div className="flex-1">
                  <h3 className="font-medium text-gray-900 dark:text-gray-100">{item.name}</h3>
                  <p className="text-sm text-gray-500 dark:text-gray-400">Qty: {item.cartQuantity}</p>
                  <p className="font-semibold mt-1">${(item.price * item.cartQuantity).toFixed(2)}</p>
                </div>
                <button 
                  onClick={() => removeFromCart(item.id)}
                  className="text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 p-2 rounded-md self-start"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            ))
          )}
        </div>

        {items.length > 0 && (
          <div className="p-4 border-t dark:border-gray-800 bg-gray-50 dark:bg-gray-900">
            <div className="flex justify-between mb-4 text-lg font-semibold">
              <span>Total</span>
              <span>${cartTotal.toFixed(2)}</span>
            </div>
            <Button className="w-full" onClick={handleCheckout}>
              Place Order
            </Button>
          </div>
        )}
      </div>
    </>
  );
}
