import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AppUtils {
  getRange(end: number, start: number = 0) {
    let arr = Array.from(Array(Math.ceil(end - start)).keys());
    return arr.map(v => v + start)
  }

  getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
  }
}
