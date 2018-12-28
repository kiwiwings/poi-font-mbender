package de.kiwiwings.sfntly.table.opentype.component;

import de.kiwiwings.sfntly.data.WritableFontData;

interface Record {
  int writeTo(WritableFontData newData, int base);
}
