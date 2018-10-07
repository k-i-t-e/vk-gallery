import { Component, OnInit } from '@angular/core';
import {GroupService} from '../service/group/group.service';
import {Group} from '../entity/Group';
import {flatMap} from "rxjs/operators";

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  favourites: Array<Group> = [];
  allGroups: Array<Group> = [];

  constructor(private groupService: GroupService) { }

  ngOnInit() {
    this.groupService.getGroups()
      .subscribe(res => {
        this.favourites = res.favourites;
        this.allGroups = res.allGroups
      })
  };

  public favGroup(group: Group) {
    this.groupService.addFavouriteGroup(group);
    this.allGroups = this.allGroups.filter(g => g !== group);
    this.favourites.push(group)
  }

  public unfavGroup(group: Group) {
    this.groupService.removeFavouriteGroup(group)
      .pipe(flatMap(ignored => this.groupService.getGroups())).subscribe(res => {
      this.favourites = res.favourites;
      this.allGroups = res.allGroups
    })
  }
}
