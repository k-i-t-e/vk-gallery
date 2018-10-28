import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AppUtils} from '../utils/app-utils.service';

@Component({
  selector: 'app-thumbnails',
  templateUrl: './thumbnails.component.html',
  styleUrls: ['./thumbnails.component.css']
})
export class ThumbnailsComponent implements OnInit {
  @Input() thumbnails: Array<String>;
  @Input() placeholderString: string;
  @Output() thumbnailClick = new EventEmitter<Number>();

  constructor(public appUtils: AppUtils) { }

  ngOnInit() {
  }

}
