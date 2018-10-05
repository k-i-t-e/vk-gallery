import { Component, OnInit } from '@angular/core';
import {GalleryService} from "../service/gallery/gallery.service";
import {Image} from "../entity/Image";
import {AppUtils} from "../utils/app-utils.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ImageModalComponent} from "../image-modal/image-modal.component";

@Component({
  selector: 'app-browser',
  templateUrl: './browser.component.html',
  styleUrls: ['./browser.component.css']
})
export class BrowserComponent implements OnInit {
  images: Array<Image> = [];

  totalPages: number = 1;
  page = 0;
  groupId: string;

  constructor(private galleryService: GalleryService, public appUtils: AppUtils, private modalService: NgbModal) { }

  ngOnInit() {
  }

  getThumbnail(index: number): string {
    if (index < this.images.length) {
      return this.images[index].urls["604"];
    }
    return null;
  }

  getLargeImage(index: number): string {
    let largest: string;
    console.log(this.images[index].urls) //TODO: fix
    for (let url of this.images[index].urls.entries()) {
      if (url.values()) {
        largest = url.values().next().value
      } else {
        return largest
      }
    }
    return largest
  }

  onPageSelected(n: number) {
    if (this.groupId) {
      this.page = n;
      this.galleryService.getImages(this.groupId, n * 100)
        .subscribe(res => {
          this.images = res.images;
          console.log(n);
          if (n + 1 > this.totalPages) {
            this.totalPages = n + 1
          }
        })
    }
  }

  onBrowse(groupId: string) {
    if (groupId) {
      this.page = 0;
      this.totalPages = 1;
      this.groupId = groupId;
      this.onPageSelected(0);
    }
  }

  openModal(index: number) {
    if (index < this.images.length) {
      const modal = this.modalService.open(ImageModalComponent);
      let url = this.getLargeImage(index)
      console.log(url)
      modal.componentInstance.imageUrl = url
    }
  }
}
