import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Image} from "../../entity/Image";
import {API_ROOT_URL} from "../constants";

@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  constructor(private http: HttpClient) { }

  public getImages(groupId: string, offset: number): Observable<Array<Image>> {
    return this.http.get<Array<Image>>(API_ROOT_URL + '/images/' + groupId)
  }
}
