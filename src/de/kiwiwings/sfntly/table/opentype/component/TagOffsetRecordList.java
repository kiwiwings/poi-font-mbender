package de.kiwiwings.sfntly.table.opentype.component;

import de.kiwiwings.sfntly.data.ReadableFontData;
import de.kiwiwings.sfntly.data.WritableFontData;

final class TagOffsetRecordList extends RecordList<TagOffsetRecord> {
  TagOffsetRecordList(WritableFontData data) {
    super(data);
  }

  TagOffsetRecordList(ReadableFontData data) {
    super(data);
  }

  static int sizeOfListOfCount(int count) {
    return RecordList.DATA_OFFSET + count * TagOffsetRecord.RECORD_SIZE;
  }

  TagOffsetRecord getRecordForTag(int tag) {
    for (TagOffsetRecord record : this) {
      if (record.tag == tag) {
        return record;
      }
    }
    return null;
  }

  @Override
  protected TagOffsetRecord getRecordAt(ReadableFontData data, int offset) {
    return new TagOffsetRecord(data, offset);
  }

  @Override
  protected int recordSize() {
    return TagOffsetRecord.RECORD_SIZE;
  }
}
