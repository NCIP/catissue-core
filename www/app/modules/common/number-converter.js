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

    function fromAlphabet(str, aCharCode) {
      var len = str.length;
      var base = 1, result = 0;

      while (len > 0) {
        len--;

        result += (str.charCodeAt(len) - aCharCode + 1) * base;
        base *= 26;
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

    function fromRoman(str, upper) {
      str = str.toUpperCase();

      var result = 0;
      var len = str.length, idx = str.length;
      while (idx > 0) {
        --idx;
        if (idx == len - 1) {
          var val = romanLookupTab[str.substring(idx, idx + 1)];
          if (!val) {
            alert("Invalid roman number: " + str);
          }

          result += val;
        } else {
          var current = romanLookupTab[str.substring(idx, idx + 1)];
          var ahead   = romanLookupTab[str.substring(idx + 1, idx + 2)];
          if (!current || !ahead) {
            alert("Invalid roman number: " + str);
          }

          if (current < ahead) {
            result -= current;
          } else {
            result += current;
          }
        }
      }

      return result;
    }

    function toNum(num) {
      return num;
    };

    function fromNum(str) {
      return +str;
    }

    function toUpperAlphabet(num) {
      return toAlphabet(num, 'A'.charCodeAt(0));
    };

    function fromUpperAlphabet(str) {
      return fromAlphabet(str, 'A'.charCodeAt(0));
    };

    function toLowerAlphabet(num) {
      return toAlphabet(num, 'a'.charCodeAt(0));
    };

    function fromLowerAlphabet(str) {
      return fromAlphabet(str, 'a'.charCodeAt(0));
    };

    function toUpperRoman(num) {
      return toRoman(num, true);
    };

    function fromUpperRoman(str) {
      return fromRoman(str, true);
    };

    function toLowerRoman(num) {
      return toRoman(num, false);
    }

    function fromLowerRoman(str) {
      return fromRoman(str, false);
    }

    var fromNumConvertersMap = {
      'Numbers'             : toNum,
      'Alphabets Upper Case': toUpperAlphabet,
      'Alphabets Lower Case': toLowerAlphabet,
      'Roman Upper Case'    : toUpperRoman,
      'Roman Lower Case'    : toLowerRoman
    };

    var toNumConvertersMap = {
      'Numbers'             : fromNum,
      'Alphabets Upper Case': fromUpperAlphabet,
      'Alphabets Lower Case': fromLowerAlphabet,
      'Roman Upper Case'    : fromUpperRoman,
      'Roman Lower Case'    : fromLowerRoman
    };

    function fromNumber(scheme, number) {
      return fromNumConvertersMap[scheme](number);
    }

    function toNumber(scheme, str) {
      return toNumConvertersMap[scheme](str);
    }

    return {
      fromNumber: fromNumber,

      toNumber: toNumber
    }
  });
