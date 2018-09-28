import { Component, OnInit } from '@angular/core';
import {GroupService} from "../service/group/group.service";
import {Group} from "../entity/Group";

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
  }

}
