import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Endpoints} from '../models/endpoints';
import {from, Observable} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  private endpoints?: Endpoints;

  constructor(
    private http: HttpClient
  ) {}

  public async provideEndpoints(): Promise<Endpoints> {
    if (this.endpoints) {
      return Promise.resolve(this.endpoints);
    }

    this.endpoints = await this.http.get<Endpoints>('./endpoints/endpoints.json').toPromise();
    if (!this.endpoints) {
      throw new Error('Failed to load endpoints');
    }
    return this.endpoints;
  }

  public withEndpoints(callback: (endpoints: Endpoints) => Observable<any>): Observable<any> {
    return from(this.provideEndpoints()).pipe(
      mergeMap(endpoints => {
        return callback(endpoints);
      })
    );
  }

}
