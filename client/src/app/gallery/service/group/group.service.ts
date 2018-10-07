import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Group, GroupResult} from '../../entity/Group';
import {API_ROOT_URL} from '../constants';
import {Observable} from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }

  public getGroups(): Observable<GroupResult> {
    return this.http.get<GroupResult>(API_ROOT_URL + '/group');
  }

  public addFavouriteGroup(group: Group) {
    this.http.post(API_ROOT_URL + '/group', group.id)
      .subscribe((ignore) => ignore, (error) => console.log(error))
  }

  public removeFavouriteGroup(group: Group): Observable<Boolean> {
    return this.http.delete<Boolean>(API_ROOT_URL + '/group/' + group.id)
  }
}
