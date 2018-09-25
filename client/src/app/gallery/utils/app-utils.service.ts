import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AppUtils {
  getRange(n: number) {
    let arr = Array.from(Array(Math.ceil(n)).keys());
    return arr
  }

  getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
  }
}
