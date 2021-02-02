(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'MortgageCalculator'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'MortgageCalculator'.");
    }root.MortgageCalculator = factory(typeof MortgageCalculator === 'undefined' ? {} : MortgageCalculator, kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var throwUPAE = Kotlin.throwUPAE;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var IntRange = Kotlin.kotlin.ranges.IntRange;
  var Unit = Kotlin.kotlin.Unit;
  var joinToString = Kotlin.kotlin.collections.joinToString_fmv235$;
  var toString = Kotlin.toString;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var Math_0 = Math;
  var round = Kotlin.kotlin.math.round_14dthe$;
  Html.prototype = Object.create(Tag.prototype);
  Html.prototype.constructor = Html;
  Table.prototype = Object.create(Tag.prototype);
  Table.prototype.constructor = Table;
  Center.prototype = Object.create(Tag.prototype);
  Center.prototype.constructor = Center;
  TR.prototype = Object.create(Tag.prototype);
  TR.prototype.constructor = TR;
  TD.prototype = Object.create(Tag.prototype);
  TD.prototype.constructor = TD;
  Text.prototype = Object.create(Tag.prototype);
  Text.prototype.constructor = Text;
  function Mortgage() {
    this.extraPaymentsSet = false;
    this.extraPaymentAmount = 0.0;
    this.length = 0;
    this.remainLength = 0;
    this.custName = '';
    this.initialBalance = 0.0;
    this.currentBalance = 0.0;
    this.downpay = 0.0;
    this.downpercent = 0.0;
    this.interestRate = 0.0;
    this.downPayAsPercent = false;
    this.amTableExist = false;
    this.amTableData_oj4734$_0 = this.amTableData_oj4734$_0;
  }
  Object.defineProperty(Mortgage.prototype, 'amTableData', {
    configurable: true,
    get: function () {
      if (this.amTableData_oj4734$_0 == null)
        return throwUPAE('amTableData');
      return this.amTableData_oj4734$_0;
    },
    set: function (amTableData) {
      this.amTableData_oj4734$_0 = amTableData;
    }
  });
  Mortgage.prototype.setDownPayment_14dthe$ = function (payment) {
    if (payment <= 0) {
      this.downPayAsPercent = false;
      this.downpay = 0.0;
      this.downpercent = 0.0;
    } else if (payment > 100) {
      this.downpay = payment;
      this.downpercent = payment / this.initialBalance;
      this.downPayAsPercent = false;
    } else {
      if (payment < 1) {
        this.downpay = payment * this.initialBalance;
        this.downpercent = payment * 100;
        this.downPayAsPercent = true;
      } else {
        this.downpay = payment / 100 * this.initialBalance;
        this.downpercent = payment * 100;
        this.downPayAsPercent = true;
      }
      this.amTableExist = false;
    }
  };
  Mortgage.prototype.setInitLength_za3lpa$ = function (len) {
    this.length = len;
    this.amTableExist = false;
  };
  Mortgage.prototype.setRemLength_za3lpa$ = function (length) {
    this.remainLength = length;
  };
  Mortgage.prototype.setCusName_61zpoe$ = function (name) {
    this.custName = name;
  };
  Mortgage.prototype.amTable = function () {
    var table;
    if (this.amTableExist) {
      table = this.amTableData;
    } else {
      table = AmmortTable(this.length, this.initialBalance, this.interestRate);
      this.amTableData = table;
      this.amTableExist = true;
    }
    return table;
  };
  Mortgage.prototype.setExtraPayment_8ca0d4$ = function (amount, enabled) {
    this.amTableExist = false;
    if (enabled) {
      this.extraPaymentsSet = false;
      this.extraPaymentAmount = amount;
    } else {
      this.extraPaymentsSet = false;
    }
  };
  Mortgage.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Mortgage',
    interfaces: []
  };
  function AmmortTable(length, initbal, interestrate, extrapayment, extrapayenabled) {
    if (extrapayment === void 0)
      extrapayment = 0.0;
    if (extrapayenabled === void 0)
      extrapayenabled = false;
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    var table = ArrayList_init();
    var currentbal = roundToTwoDecimalPlace(initbal);
    table.add_wxm5ur$(0, ArrayList_init());
    table.get_za3lpa$(0).add_wxm5ur$(0, 0);
    var periodicinterest = interestrate / 12 / 100;
    var tmp$_3 = initbal * periodicinterest;
    var $receiver = 1 + periodicinterest;
    var payment = tmp$_3 / (1 - 1 / Math_0.pow($receiver, length));
    table.get_za3lpa$(0).add_wxm5ur$(1, payment);
    table.get_za3lpa$(0).add_wxm5ur$(2, 0);
    table.get_za3lpa$(0).add_wxm5ur$(3, roundToTwoDecimalPlace(0));
    table.get_za3lpa$(0).add_wxm5ur$(4, roundToTwoDecimalPlace(0));
    if (extrapayenabled) {
      payment += roundToTwoDecimalPlace(extrapayment);
      table.get_za3lpa$(0).set_wxm5ur$(4, roundToTwoDecimalPlace(extrapayment));
    }var balanceWillBeZero = false;
    tmp$ = new IntRange(1, length);
    tmp$_0 = tmp$.first;
    tmp$_1 = tmp$.last;
    tmp$_2 = tmp$.step;
    for (var i = tmp$_0; i <= tmp$_1; i += tmp$_2) {
      var interestPayment = roundToTwoDecimalPlace(currentbal * periodicinterest);
      var principalPayment = roundToTwoDecimalPlace(payment - interestPayment);
      if (extrapayenabled) {
        if (principalPayment >= currentbal) {
          principalPayment = currentbal;
          balanceWillBeZero = true;
        }}currentbal -= principalPayment;
      var listyBoi = ArrayList_init();
      listyBoi.add_wxm5ur$(0, roundToTwoDecimalPlace(i));
      listyBoi.add_wxm5ur$(1, roundToTwoDecimalPlace(payment));
      listyBoi.add_wxm5ur$(2, roundToTwoDecimalPlace(interestPayment));
      listyBoi.add_wxm5ur$(3, roundToTwoDecimalPlace(principalPayment));
      listyBoi.add_wxm5ur$(4, roundToTwoDecimalPlace(currentbal));
      listyBoi.add_wxm5ur$(5, roundToTwoDecimalPlace(extrapayment));
      table.add_wxm5ur$(i, listyBoi);
      table.get_za3lpa$(0).set_wxm5ur$(0, i);
      table.get_za3lpa$(0).set_wxm5ur$(2, table.get_za3lpa$(0).get_za3lpa$(2) + interestPayment);
      table.get_za3lpa$(0).set_wxm5ur$(3, table.get_za3lpa$(0).get_za3lpa$(3) + principalPayment);
      if (balanceWillBeZero) {
        table.get_za3lpa$(0).set_wxm5ur$(2, roundToTwoDecimalPlace(table.get_za3lpa$(0).get_za3lpa$(2)));
        table.get_za3lpa$(0).set_wxm5ur$(3, roundToTwoDecimalPlace(table.get_za3lpa$(0).get_za3lpa$(3)));
        return table;
      }}
    table.get_za3lpa$(0).set_wxm5ur$(2, roundToTwoDecimalPlace(table.get_za3lpa$(0).get_za3lpa$(2)));
    table.get_za3lpa$(0).set_wxm5ur$(3, roundToTwoDecimalPlace(table.get_za3lpa$(0).get_za3lpa$(3)));
    return table;
  }
  function roundToTwoDecimalPlace($receiver) {
    var df = $receiver * 100;
    df = round(df);
    df = df / 100;
    return df;
  }
  function dataSubmission(initbal, term, interestrate, extrapayment) {
    var table;
    if (extrapayment === 0.0) {
      table = AmmortTable(term, initbal, interestrate);
    } else {
      table = AmmortTable(term, initbal, interestrate, extrapayment, true);
    }
    var resultantTable = tableRender(table);
    return resultantTable;
  }
  function tableRender$lambda$lambda$lambda$lambda($receiver) {
    text($receiver, 'Month');
    return Unit;
  }
  function tableRender$lambda$lambda$lambda$lambda_0($receiver) {
    text($receiver, 'Interest Paid');
    return Unit;
  }
  function tableRender$lambda$lambda$lambda$lambda_1($receiver) {
    text($receiver, 'Principal Paid');
    return Unit;
  }
  function tableRender$lambda$lambda$lambda$lambda_2($receiver) {
    text($receiver, 'Extra Payment');
    return Unit;
  }
  function tableRender$lambda$lambda$lambda$lambda_3($receiver) {
    text($receiver, 'Total Payment');
    return Unit;
  }
  function tableRender$lambda$lambda$lambda($receiver) {
    td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda);
    td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_0);
    td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_1);
    td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_2);
    td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_3);
    return Unit;
  }
  function tableRender$lambda$lambda$lambda$lambda_4(closure$item) {
    return function ($receiver) {
      closure$item.get_za3lpa$(0);
      return Unit;
    };
  }
  function tableRender$lambda$lambda$lambda$lambda_5(closure$item) {
    return function ($receiver) {
      closure$item.get_za3lpa$(2);
      return Unit;
    };
  }
  function tableRender$lambda$lambda$lambda$lambda_6(closure$item) {
    return function ($receiver) {
      closure$item.get_za3lpa$(3);
      return Unit;
    };
  }
  function tableRender$lambda$lambda$lambda$lambda_7(closure$item) {
    return function ($receiver) {
      closure$item.get_za3lpa$(5);
      return Unit;
    };
  }
  function tableRender$lambda$lambda$lambda$lambda_8(closure$item) {
    return function ($receiver) {
      closure$item.get_za3lpa$(1);
      return Unit;
    };
  }
  function tableRender$lambda$lambda$lambda_0(closure$item) {
    return function ($receiver) {
      td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_4(closure$item));
      td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_5(closure$item));
      td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_6(closure$item));
      td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_7(closure$item));
      td($receiver, void 0, void 0, tableRender$lambda$lambda$lambda$lambda_8(closure$item));
      return Unit;
    };
  }
  function tableRender$lambda$lambda(closure$ammorttable) {
    return function ($receiver) {
      var tmp$;
      tr($receiver, void 0, tableRender$lambda$lambda$lambda);
      var ammort = closure$ammorttable;
      tmp$ = ammort.iterator();
      while (tmp$.hasNext()) {
        var item = tmp$.next();
        tr($receiver, void 0, tableRender$lambda$lambda$lambda_0(item));
      }
      return Unit;
    };
  }
  function tableRender$lambda(closure$ammorttable) {
    return function ($receiver) {
      table($receiver, tableRender$lambda$lambda(closure$ammorttable));
      return Unit;
    };
  }
  function tableRender(ammorttable) {
    var result = html(tableRender$lambda(ammorttable));
    return result;
  }
  function Tag(name) {
    this.name = name;
    this.children = ArrayList_init();
    this.attributes = ArrayList_init();
  }
  Tag.prototype.toString = function () {
    return '<' + this.name + (this.attributes.isEmpty() ? '' : joinToString(this.attributes, ' ', ' ')) + '>' + (this.children.isEmpty() ? '' : joinToString(this.children, '')) + ('<\/' + this.name + '>');
  };
  Tag.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Tag',
    interfaces: []
  };
  function Attribute(name, value) {
    this.name = name;
    this.value = value;
  }
  Attribute.prototype.toString = function () {
    return this.name + '=' + '"' + this.value + '"';
  };
  Attribute.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Attribute',
    interfaces: []
  };
  function set($receiver, name, value) {
    if (value != null) {
      $receiver.attributes.add_11rb$(new Attribute(name, value));
    }return $receiver;
  }
  function doInit($receiver, tag, init) {
    init(tag);
    $receiver.children.add_11rb$(tag);
    return tag;
  }
  function Html() {
    Tag.call(this, 'html');
  }
  Html.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Html',
    interfaces: [Tag]
  };
  function Table() {
    Tag.call(this, 'table');
  }
  Table.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Table',
    interfaces: [Tag]
  };
  function Center() {
    Tag.call(this, 'center');
  }
  Center.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Center',
    interfaces: [Tag]
  };
  function TR() {
    Tag.call(this, 'tr');
  }
  TR.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'TR',
    interfaces: [Tag]
  };
  function TD() {
    Tag.call(this, 'td');
  }
  TD.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'TD',
    interfaces: [Tag]
  };
  function Text(text) {
    Tag.call(this, 'b');
    this.text = text;
  }
  Text.prototype.toString = function () {
    return this.text;
  };
  Text.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Text',
    interfaces: [Tag]
  };
  function html(init) {
    var $receiver = new Html();
    init($receiver);
    return $receiver;
  }
  function table($receiver, init) {
    return doInit($receiver, new Table(), init);
  }
  function center($receiver, init) {
    return doInit($receiver, new Center(), init);
  }
  function tr($receiver, color, init) {
    if (color === void 0)
      color = null;
    return set(doInit($receiver, new TR(), init), 'bgcolor', color);
  }
  function td($receiver, color, align, init) {
    if (color === void 0)
      color = null;
    if (align === void 0)
      align = 'left';
    return set(set(doInit($receiver, new TD(), init), 'align', align), 'bgcolor', color);
  }
  function text$lambda($receiver) {
    return Unit;
  }
  function text($receiver, s) {
    return doInit($receiver, new Text(toString(s)), text$lambda);
  }
  _.Mortgage = Mortgage;
  _.AmmortTable_1duvf6$ = AmmortTable;
  _.roundToTwoDecimalPlace_81szk$ = roundToTwoDecimalPlace;
  _.dataSubmission_a5zayn$ = dataSubmission;
  _.tableRender = tableRender;
  _.Tag = Tag;
  _.Attribute = Attribute;
  _.set_csunl2$ = set;
  _.doInit_xd45vc$ = doInit;
  _.Html = Html;
  _.Table = Table;
  _.Center = Center;
  _.TR = TR;
  _.TD = TD;
  _.Text = Text;
  _.html_4vm6gk$ = html;
  _.table_did6br$ = table;
  _.center_gf29lq$ = center;
  _.tr_uu6uc1$ = tr;
  _.td_iho22p$ = td;
  _.text_37t48b$ = text;
  Kotlin.defineModule('MortgageCalculator', _);
  return _;
}));

//# sourceMappingURL=MortgageCalculator.js.map
