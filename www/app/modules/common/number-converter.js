angular.module('openspecimen')
  .factory('NumberConverterUtil', function() {

    function toAlphabet(num, aCharCode) {
      var result = "";
      while (num > 0) {
        result = String.fromCharCode((num - 1) % 26 + aCharCode) + result;
        num = Math.floor((num - 1) / 26);
      }

      return result;
    };

    var romanLookupTab = {
      M: 1000, CM: 900,
      D:  500, CD: 400,
      C:  100, XC:  90,
      L:   50, XL:  40,
      X:   10, IX:   9,
      V:    5, IV:   4,
      I:    1
    };

    function toRoman(num, upper) {
      var result = '';
      for (var i in romanLookupTab) {
        while (num >= romanLookupTab[i]) {
          result += i;
          num -= romanLookupTab[i];
        }
      }
 
      return upper ? result : result.toLowerCase();
    };

    function toNum(num) {
      return num;
    };

    function toUpperAlphabet(num) {
      return toAlphabet(num, 'A'.charCodeAt(0));
    };

    function toLowerAlphabet(num) {
      return toAlphabet(num, 'a'.charCodeAt(0));
    };

    function toUpperRoman(num) {
      return toRoman(num, true);
    };

    function toLowerRoman(num) {
      return toRoman(num, false);
    }

    var convertersMap = {
      'Numbers'             : toNum,
      'Alphabets Upper Case': toUpperAlphabet,
      'Alphabets Lower Case': toLowerAlphabet,
      'Roman Upper Case'    : toUpperRoman,
      'Roman Lower Case'    : toLowerRoman
    };

    function fromNumber(scheme, number) {
      return convertersMap[scheme](number);
    }

    return {
      fromNumber: fromNumber 
    }
  });
