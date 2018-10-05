import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AppUtils} from "../utils/app-utils.service";
import {browser} from "protractor";

@Component({
  selector: 'app-browser-controls',
  templateUrl: './browser-controls.component.html',
  styleUrls: ['./browser-controls.component.css']
})
export class BrowserControlsComponent implements OnInit {

  constructor(public appUtils: AppUtils) { }

  // Pagination fields TODO: move to a separate component
  page = 0;
  @Input() totalPages = 1;
  @Output() pageSelected = new EventEmitter<Number>();
  showSpaceBefore = false;
  showFirst = false;
  showSpaceAfter = false;
  showLast = false;

  // Search form fields
  groupId: string;
  @Output() browse = new EventEmitter<String>();

  ngOnInit() {
  }

  public selectPage(n: number) {
    if (this.groupId) {
      console.log(n);
      this.page = n;
      this.pageSelected.emit(n);
    }
  }

  public selectPreviousPage() {
    if (this.page > 0) {
      this.selectPage(this.page - 1)
    }
  }

  public browseClick() {
    if (this.groupId && this.groupId.length > 0) {
      this.browse.emit(this.groupId);
    }
  }

  public getMiddleRange() {
    const start = Math.max(0, this.page - 2);
    const end = Math.min(this.totalPages, this.page + 3);
    this.showFirst = start > 0;
    this.showSpaceBefore = start > 1;
    this.showLast = end < this.totalPages;
    this.showSpaceAfter = end < this.totalPages - 1;
    return this.appUtils.getRange(end, start)
  }
}
