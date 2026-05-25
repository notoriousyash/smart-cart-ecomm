export interface User {
  id: number;
  username: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  message: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
}

export interface CartItem extends Product {
  cartQuantity: number;
}

export interface OrderRequest {
  userId: number;
  productId: number;
  quantity: number;
}
