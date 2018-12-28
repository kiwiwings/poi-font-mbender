// Copyright 2012 Google Inc. All Rights Reserved.

package de.kiwiwings.sfntly.table.opentype;

import de.kiwiwings.sfntly.data.ReadableFontData;

/** @author dougfelt@google.com (Doug Felt) */
abstract class GsubLookupTable extends LookupTable {

  protected GsubLookupTable(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  abstract static class Builder<T extends GsubLookupTable> extends LookupTable.Builder {

    protected Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    protected Builder() {}

    protected Builder(T table) {
      super(table);
    }
  }
}
