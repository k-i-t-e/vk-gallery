import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Album} from '../../entity/Album';
import {API_ROOT_URL} from '../constants';
import {map, tap} from 'rxjs/operators';
import {Image} from '../../entity/Image';

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

  public addAlbum(albumName: string): Observable<Array<Album>> {
    return this.http.post<Album>(API_ROOT_URL + '/album', albumName)
      .pipe(
        map(newAlbum => {
          this.albumsCache.push(newAlbum);
          return this.albumsCache.sort((a1, a2) => a1.name.localeCompare(a2.name))
        })
      )
  }

  public addImage(albumId: number, image: Image): Observable<Boolean> {
    return this.http.put<Boolean>(API_ROOT_URL + `/album/${albumId}/image`, image)
  }
}
