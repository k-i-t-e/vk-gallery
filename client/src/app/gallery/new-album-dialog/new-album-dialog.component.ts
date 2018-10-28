import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-album-dialog',
  templateUrl: './new-album-dialog.component.html',
  styleUrls: ['./new-album-dialog.component.css']
})
export class NewAlbumDialogComponent implements OnInit {
  albumName: string;
  errorMessage: string;
  constructor() { }

  ngOnInit() {
  }

  onOk() {
    if (!this.albumName || this.albumName.length === 0) {
      this.errorMessage = 'Album name cannot be null'
    } else {
      this.errorMessage = undefined
    }
  }

  onCancel() {

  }
}
