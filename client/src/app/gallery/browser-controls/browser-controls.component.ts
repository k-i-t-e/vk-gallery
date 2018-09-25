import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AppUtils} from "../utils/app-utils.service";

@Component({
  selector: 'app-browser-controls',
  templateUrl: './browser-controls.component.html',
  styleUrls: ['./browser-controls.component.css']
})
export class BrowserControlsComponent implements OnInit {

  constructor(public appUtils: AppUtils) { }

  public page = 0;
  @Input() public totalPages = 1;
  @Output() pageSelected = new EventEmitter<Number>();

  ngOnInit() {
  }

  public selectPage(n: number) {
    console.log(n);
    this.page = n;
    this.pageSelected.emit(n);
  }

  public selectPreviousPage() {
    if (this.page > 0) {
      this.selectPage(this.page - 1)
    }
  }
}
