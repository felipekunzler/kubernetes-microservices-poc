import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
    private http: HttpClient
  ) {
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>('http://localhost:4200/api/product');
  }

  getProduct(id: string): Observable<Product> {
    return this.http.get<Product>('http://localhost:4200/api/product/' + id);
  }

}
