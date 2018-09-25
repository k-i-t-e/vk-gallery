import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {VkUser} from "../../entity/VkUser";
import {API_ROOT_URL} from "../constants";
import {catchError} from "rxjs/operators";
import {of} from "rxjs/internal/observable/of";
import {AppUtils} from "../../utils/app-utils.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http:HttpClient) { }

  public getCurrenUser(): Observable<VkUser> {
    return this.http.get<VkUser>(API_ROOT_URL + '/user/current')
      .pipe(
        catchError(this.nullErrorCatch)
      )
  }

  private nullErrorCatch(e) {
    console.log(e);
    return of(null)
  };

  public logout(): Observable<Object> {
    return this.http.post<Object>('/logout', {}).pipe(catchError(this.nullErrorCatch))
  }
}
