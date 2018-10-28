import {Component, Input, OnInit} from '@angular/core';
import {AppUtils} from '../utils/app-utils.service';

@Component({
  selector: 'app-thumbnails',
  templateUrl: './thumbnails.component.html',
  styleUrls: ['./thumbnails.component.css']
})
export class ThumbnailsComponent implements OnInit {
  @Input() thumbnails: Array<String>;
  @Input() placeholderString: string;
  constructor(public appUtils: AppUtils) { }

  ngOnInit() {
  }

}
