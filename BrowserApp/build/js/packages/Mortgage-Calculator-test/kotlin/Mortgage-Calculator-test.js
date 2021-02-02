(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin', 'Mortgage-Calculator', 'kotlin-test'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'), require('Mortgage-Calculator'), require('kotlin-test'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'Mortgage-Calculator-test'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'Mortgage-Calculator-test'.");
    }if (typeof this['Mortgage-Calculator'] === 'undefined') {
      throw new Error("Error loading module 'Mortgage-Calculator-test'. Its dependency 'Mortgage-Calculator' was not found. Please, check whether 'Mortgage-Calculator' is loaded prior to 'Mortgage-Calculator-test'.");
    }if (typeof this['kotlin-test'] === 'undefined') {
      throw new Error("Error loading module 'Mortgage-Calculator-test'. Its dependency 'kotlin-test' was not found. Please, check whether 'kotlin-test' is loaded prior to 'Mortgage-Calculator-test'.");
    }root['Mortgage-Calculator-test'] = factory(typeof this['Mortgage-Calculator-test'] === 'undefined' ? {} : this['Mortgage-Calculator-test'], kotlin, this['Mortgage-Calculator'], this['kotlin-test']);
  }
}(this, function (_, Kotlin, $module$Mortgage_Calculator, $module$kotlin_test) {
  'use strict';
  var AmmortTable = $module$Mortgage_Calculator.AmmortTable_1duvf6$;
  var assertEquals = $module$kotlin_test.kotlin.test.assertEquals_3m0tl5$;
  var last = Kotlin.kotlin.collections.last_2p1efm$;
  var roundToTwoDecimalPlace = $module$Mortgage_Calculator.roundToTwoDecimalPlace_81szk$;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var test = $module$kotlin_test.kotlin.test.test;
  var suite = $module$kotlin_test.kotlin.test.suite;
  var toDouble = Kotlin.kotlin.text.toDouble_pdl1vz$;
  function TestClient() {
  }
  TestClient.prototype.testAmTable = function () {
    var table = AmmortTable(360, 312500.0, 3.5);
    assertEquals(192676.55, table.get_za3lpa$(0).get_za3lpa$(2));
  };
  TestClient.prototype.testAmTableExtraPayment = function () {
    var table = AmmortTable(360, 312500.0, 3.5, 300, true);
    assertEquals(135470, table.get_za3lpa$(0).get_za3lpa$(2));
    assertEquals(264, last(table).get_za3lpa$(0));
  };
  TestClient.prototype.testRounding = function () {
    var number = 3.1415927;
    number = roundToTwoDecimalPlace(number);
    assertEquals(toDouble('3.14'), number);
  };
  TestClient.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'TestClient',
    interfaces: []
  };
  _.TestClient = TestClient;
  suite('', false, function () {
    suite('TestClient', false, function () {
      test('testAmTable', false, function () {
        return (new TestClient()).testAmTable();
      });
      test('testAmTableExtraPayment', false, function () {
        return (new TestClient()).testAmTableExtraPayment();
      });
      test('testRounding', false, function () {
        return (new TestClient()).testRounding();
      });
    });
  });
  Kotlin.defineModule('Mortgage-Calculator-test', _);
  return _;
}));

//# sourceMappingURL=Mortgage-Calculator-test.js.map
