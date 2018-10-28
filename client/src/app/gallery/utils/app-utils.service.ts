import {Injectable} from '@angular/core';
import {Image} from "../entity/Image";

@Injectable({
  providedIn: 'root'
})
export class AppUtils {
  getRange(end: number, start: number = 0) {
    const arr = Array.from(Array(Math.ceil(end - start)).keys());
    return arr.map(v => v + start)
  }

  getCookie(name) {
    const value = '; ' + document.cookie;
    const parts = value.split('; ' + name + '=');
    if (parts.length === 2) { return parts.pop().split(';').shift(); }
  }

  getLargeImage(image: Image): string {
    let largest: string;
    const entries = new Map(Object.entries(image.urls));

    for (const url of entries.keys()) {
      if (entries.get(url)) {
        largest = url
      } else {
        return entries.get(largest)
      }
      console.log(largest)
    }

    return entries.get(largest)
  }
}
