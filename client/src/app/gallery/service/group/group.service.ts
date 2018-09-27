import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GroupResult} from "../../entity/Group";
import {API_ROOT_URL} from "../constants";
import {Observable} from "rxjs/internal/Observable";

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }

  public getGroups(): Observable<GroupResult> {
    return this.http.get<GroupResult>(API_ROOT_URL + '/group');
  }
}
