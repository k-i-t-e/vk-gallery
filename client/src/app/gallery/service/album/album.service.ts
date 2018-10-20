import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Album} from '../../entity/Album';
import {API_ROOT_URL} from '../constants';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AlbumService {
  private albumsCache: Array<Album>;
  constructor(private http: HttpClient) { }

  public getAlbums(): Observable<Array<Album>> {
    if (this.albumsCache) {
      return of(this.albumsCache);
    }

    return this.http.get<Array<Album>>(API_ROOT_URL + '/album').pipe(
      tap(albums => this.albumsCache = albums)
    );
  }
}
