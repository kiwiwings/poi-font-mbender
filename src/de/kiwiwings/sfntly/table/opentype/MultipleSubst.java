package de.kiwiwings.sfntly.table.opentype;

import de.kiwiwings.sfntly.data.ReadableFontData;
import de.kiwiwings.sfntly.table.opentype.component.OneToManySubst;

public class MultipleSubst extends OneToManySubst {
  MultipleSubst(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }
}
