import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  constructor() {
  }

  cartIdSessionKey = 'cartId';

  getCurrentCartId(): string | null {
    return localStorage.getItem(this.cartIdSessionKey);
  }

  setCurrentCartId(cartId: string): void {
    localStorage.setItem(this.cartIdSessionKey, cartId);
  }

  removeSessionCart(): void {
    localStorage.removeItem(this.cartIdSessionKey);
  }

}
