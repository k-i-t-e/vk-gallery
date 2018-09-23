import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {API_ROOT_URL} from "../constants";
import {GalleryResult} from "../../entity/GalleryResult";

@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  constructor(private http: HttpClient) { }

  public getImages(groupId: string, offset: number): Observable<GalleryResult> {
    return this.http.get<GalleryResult>(API_ROOT_URL + '/images/' + groupId)
  }
}
