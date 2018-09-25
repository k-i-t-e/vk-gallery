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

  totalPages: number = 1;
  page = 0;
  groupId: string = "habr";

  constructor(private galleryService: GalleryService) { }

  ngOnInit() {
    this.galleryService.getImages(this.groupId, 0).subscribe(res => this.images = this.images.concat(res.images))
  }

  getThumbnail(index: number): string {
    if (index < this.images.length) {
      return this.images[index].urls["604"];
    }
    return null;
  }

  getRange(n: number) {
    let arr = Array.from(Array(Math.ceil(n)).keys());
    return arr
  }

  onPageSelected(n: number) {
    this.page = n;
    this.galleryService.getImages(this.groupId, n * 100)
      .subscribe(res => {
        this.images = res.images;
        console.log(n)
        if (n + 1 > this.totalPages) {
          this.totalPages = n + 1
        }
      })
  }

  onBrowse(groupId: string) {
    this.page = 0;
    this.totalPages = 1;
    this.groupId = groupId;
    this.onPageSelected(0);
  }
}
