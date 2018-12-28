package de.kiwiwings.sfntly.table.opentype.component;

import de.kiwiwings.sfntly.data.ReadableFontData;
import de.kiwiwings.sfntly.data.WritableFontData;

public class GlyphClassList extends NumRecordList {
  private GlyphClassList(WritableFontData data) {
    super(data);
  }

  private GlyphClassList(ReadableFontData data) {
    super(data);
  }

  private GlyphClassList(ReadableFontData data, int countDecrement) {
    super(data, countDecrement);
  }

  private GlyphClassList(
      ReadableFontData data, int countDecrement, int countOffset, int valuesOffset) {
    super(data, countDecrement, countOffset, valuesOffset);
  }

  public GlyphClassList(NumRecordList other) {
    super(other);
  }

  public static int sizeOfListOfCount(int count) {
    return DATA_OFFSET + count * NumRecord.RECORD_SIZE;
  }
}
