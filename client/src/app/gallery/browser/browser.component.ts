import { Component, OnInit } from '@angular/core';
import {GalleryService} from "../service/gallery/gallery.service";
import {Image} from "../entity/Image";

@Component({
  selector: 'app-browser',
  templateUrl: './browser.component.html',
  styleUrls: ['./browser.component.css']
})
export class BrowserComponent implements OnInit {
  images: Array<Image> = [];
  constructor(private galleryService: GalleryService) { }

  ngOnInit() {
    this.galleryService.getImages("habr", 0).subscribe(res => this.images = this.images.concat(res.images))
  }

  getThumbnail(index: number): string {
    if (index < this.images.length) {
      return this.images[index].urls["130"];
    }
    return null;
  }

  getRange(n: number) {
    let arr = Array.from(Array(Math.ceil(n)).keys());
    console.log(arr)
    return arr
  }
}
