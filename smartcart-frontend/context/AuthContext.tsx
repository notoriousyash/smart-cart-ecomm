"use client";

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import Cookies from 'js-cookie';

interface User {
  id?: number;
  username: string;
  role?: string;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (token: string, username: string, userId?: number, role?: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    // Check cookies on mount
    const savedToken = Cookies.get('token');
    const savedUsername = Cookies.get('username');
    const savedUserId = Cookies.get('userId');
    const savedRole = Cookies.get('role');
    
    if (savedToken && savedUsername) {
      setToken(savedToken);
      setUser({ 
        username: savedUsername, 
        id: savedUserId ? parseInt(savedUserId) : undefined,
        role: savedRole
      });
    }
  }, []);

  const login = (newToken: string, username: string, userId?: number, role?: string) => {
    setToken(newToken);
    setUser({ username, id: userId, role });
    
    // Store in cookies
    Cookies.set('token', newToken, { expires: 7 }); // expires in 7 days
    Cookies.set('username', username, { expires: 7 });
    if (userId) {
      Cookies.set('userId', userId.toString(), { expires: 7 });
    }
    if (role) {
      Cookies.set('role', role, { expires: 7 });
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    Cookies.remove('token');
    Cookies.remove('username');
    Cookies.remove('userId');
    Cookies.remove('role');
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
