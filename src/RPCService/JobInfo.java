/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package RPCService;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2017-2-16")
public class JobInfo implements org.apache.thrift.TBase<JobInfo, JobInfo._Fields>, java.io.Serializable, Cloneable, Comparable<JobInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("JobInfo");

  private static final org.apache.thrift.protocol.TField JOB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("jobId", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField APP_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("appId", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField START_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("startTime", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField FINISH_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("finishTime", org.apache.thrift.protocol.TType.I64, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new JobInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new JobInfoTupleSchemeFactory());
  }

  public int jobId; // required
  public String appId; // required
  public String status; // required
  public long startTime; // required
  public long finishTime; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    JOB_ID((short)1, "jobId"),
    APP_ID((short)2, "appId"),
    STATUS((short)3, "status"),
    START_TIME((short)4, "startTime"),
    FINISH_TIME((short)5, "finishTime");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // JOB_ID
          return JOB_ID;
        case 2: // APP_ID
          return APP_ID;
        case 3: // STATUS
          return STATUS;
        case 4: // START_TIME
          return START_TIME;
        case 5: // FINISH_TIME
          return FINISH_TIME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __JOBID_ISSET_ID = 0;
  private static final int __STARTTIME_ISSET_ID = 1;
  private static final int __FINISHTIME_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.JOB_ID, new org.apache.thrift.meta_data.FieldMetaData("jobId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.APP_ID, new org.apache.thrift.meta_data.FieldMetaData("appId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.START_TIME, new org.apache.thrift.meta_data.FieldMetaData("startTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.FINISH_TIME, new org.apache.thrift.meta_data.FieldMetaData("finishTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(JobInfo.class, metaDataMap);
  }

  public JobInfo() {
  }

  public JobInfo(
    int jobId,
    String appId,
    String status,
    long startTime,
    long finishTime)
  {
    this();
    this.jobId = jobId;
    setJobIdIsSet(true);
    this.appId = appId;
    this.status = status;
    this.startTime = startTime;
    setStartTimeIsSet(true);
    this.finishTime = finishTime;
    setFinishTimeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public JobInfo(JobInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.jobId = other.jobId;
    if (other.isSetAppId()) {
      this.appId = other.appId;
    }
    if (other.isSetStatus()) {
      this.status = other.status;
    }
    this.startTime = other.startTime;
    this.finishTime = other.finishTime;
  }

  public JobInfo deepCopy() {
    return new JobInfo(this);
  }

  @Override
  public void clear() {
    setJobIdIsSet(false);
    this.jobId = 0;
    this.appId = null;
    this.status = null;
    setStartTimeIsSet(false);
    this.startTime = 0;
    setFinishTimeIsSet(false);
    this.finishTime = 0;
  }

  public int getJobId() {
    return this.jobId;
  }

  public JobInfo setJobId(int jobId) {
    this.jobId = jobId;
    setJobIdIsSet(true);
    return this;
  }

  public void unsetJobId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __JOBID_ISSET_ID);
  }

  /** Returns true if field jobId is set (has been assigned a value) and false otherwise */
  public boolean isSetJobId() {
    return EncodingUtils.testBit(__isset_bitfield, __JOBID_ISSET_ID);
  }

  public void setJobIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __JOBID_ISSET_ID, value);
  }

  public String getAppId() {
    return this.appId;
  }

  public JobInfo setAppId(String appId) {
    this.appId = appId;
    return this;
  }

  public void unsetAppId() {
    this.appId = null;
  }

  /** Returns true if field appId is set (has been assigned a value) and false otherwise */
  public boolean isSetAppId() {
    return this.appId != null;
  }

  public void setAppIdIsSet(boolean value) {
    if (!value) {
      this.appId = null;
    }
  }

  public String getStatus() {
    return this.status;
  }

  public JobInfo setStatus(String status) {
    this.status = status;
    return this;
  }

  public void unsetStatus() {
    this.status = null;
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return this.status != null;
  }

  public void setStatusIsSet(boolean value) {
    if (!value) {
      this.status = null;
    }
  }

  public long getStartTime() {
    return this.startTime;
  }

  public JobInfo setStartTime(long startTime) {
    this.startTime = startTime;
    setStartTimeIsSet(true);
    return this;
  }

  public void unsetStartTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STARTTIME_ISSET_ID);
  }

  /** Returns true if field startTime is set (has been assigned a value) and false otherwise */
  public boolean isSetStartTime() {
    return EncodingUtils.testBit(__isset_bitfield, __STARTTIME_ISSET_ID);
  }

  public void setStartTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STARTTIME_ISSET_ID, value);
  }

  public long getFinishTime() {
    return this.finishTime;
  }

  public JobInfo setFinishTime(long finishTime) {
    this.finishTime = finishTime;
    setFinishTimeIsSet(true);
    return this;
  }

  public void unsetFinishTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __FINISHTIME_ISSET_ID);
  }

  /** Returns true if field finishTime is set (has been assigned a value) and false otherwise */
  public boolean isSetFinishTime() {
    return EncodingUtils.testBit(__isset_bitfield, __FINISHTIME_ISSET_ID);
  }

  public void setFinishTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __FINISHTIME_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case JOB_ID:
      if (value == null) {
        unsetJobId();
      } else {
        setJobId((Integer)value);
      }
      break;

    case APP_ID:
      if (value == null) {
        unsetAppId();
      } else {
        setAppId((String)value);
      }
      break;

    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((String)value);
      }
      break;

    case START_TIME:
      if (value == null) {
        unsetStartTime();
      } else {
        setStartTime((Long)value);
      }
      break;

    case FINISH_TIME:
      if (value == null) {
        unsetFinishTime();
      } else {
        setFinishTime((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case JOB_ID:
      return Integer.valueOf(getJobId());

    case APP_ID:
      return getAppId();

    case STATUS:
      return getStatus();

    case START_TIME:
      return Long.valueOf(getStartTime());

    case FINISH_TIME:
      return Long.valueOf(getFinishTime());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case JOB_ID:
      return isSetJobId();
    case APP_ID:
      return isSetAppId();
    case STATUS:
      return isSetStatus();
    case START_TIME:
      return isSetStartTime();
    case FINISH_TIME:
      return isSetFinishTime();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof JobInfo)
      return this.equals((JobInfo)that);
    return false;
  }

  public boolean equals(JobInfo that) {
    if (that == null)
      return false;

    boolean this_present_jobId = true;
    boolean that_present_jobId = true;
    if (this_present_jobId || that_present_jobId) {
      if (!(this_present_jobId && that_present_jobId))
        return false;
      if (this.jobId != that.jobId)
        return false;
    }

    boolean this_present_appId = true && this.isSetAppId();
    boolean that_present_appId = true && that.isSetAppId();
    if (this_present_appId || that_present_appId) {
      if (!(this_present_appId && that_present_appId))
        return false;
      if (!this.appId.equals(that.appId))
        return false;
    }

    boolean this_present_status = true && this.isSetStatus();
    boolean that_present_status = true && that.isSetStatus();
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (!this.status.equals(that.status))
        return false;
    }

    boolean this_present_startTime = true;
    boolean that_present_startTime = true;
    if (this_present_startTime || that_present_startTime) {
      if (!(this_present_startTime && that_present_startTime))
        return false;
      if (this.startTime != that.startTime)
        return false;
    }

    boolean this_present_finishTime = true;
    boolean that_present_finishTime = true;
    if (this_present_finishTime || that_present_finishTime) {
      if (!(this_present_finishTime && that_present_finishTime))
        return false;
      if (this.finishTime != that.finishTime)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_jobId = true;
    list.add(present_jobId);
    if (present_jobId)
      list.add(jobId);

    boolean present_appId = true && (isSetAppId());
    list.add(present_appId);
    if (present_appId)
      list.add(appId);

    boolean present_status = true && (isSetStatus());
    list.add(present_status);
    if (present_status)
      list.add(status);

    boolean present_startTime = true;
    list.add(present_startTime);
    if (present_startTime)
      list.add(startTime);

    boolean present_finishTime = true;
    list.add(present_finishTime);
    if (present_finishTime)
      list.add(finishTime);

    return list.hashCode();
  }

  @Override
  public int compareTo(JobInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetJobId()).compareTo(other.isSetJobId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJobId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jobId, other.jobId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAppId()).compareTo(other.isSetAppId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAppId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.appId, other.appId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStartTime()).compareTo(other.isSetStartTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStartTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.startTime, other.startTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetFinishTime()).compareTo(other.isSetFinishTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFinishTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.finishTime, other.finishTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("JobInfo(");
    boolean first = true;

    sb.append("jobId:");
    sb.append(this.jobId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("appId:");
    if (this.appId == null) {
      sb.append("null");
    } else {
      sb.append(this.appId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("status:");
    if (this.status == null) {
      sb.append("null");
    } else {
      sb.append(this.status);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("startTime:");
    sb.append(this.startTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("finishTime:");
    sb.append(this.finishTime);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class JobInfoStandardSchemeFactory implements SchemeFactory {
    public JobInfoStandardScheme getScheme() {
      return new JobInfoStandardScheme();
    }
  }

  private static class JobInfoStandardScheme extends StandardScheme<JobInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, JobInfo struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // JOB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.jobId = iprot.readI32();
              struct.setJobIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // APP_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.appId = iprot.readString();
              struct.setAppIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.status = iprot.readString();
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // START_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.startTime = iprot.readI64();
              struct.setStartTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // FINISH_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.finishTime = iprot.readI64();
              struct.setFinishTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, JobInfo struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(JOB_ID_FIELD_DESC);
      oprot.writeI32(struct.jobId);
      oprot.writeFieldEnd();
      if (struct.appId != null) {
        oprot.writeFieldBegin(APP_ID_FIELD_DESC);
        oprot.writeString(struct.appId);
        oprot.writeFieldEnd();
      }
      if (struct.status != null) {
        oprot.writeFieldBegin(STATUS_FIELD_DESC);
        oprot.writeString(struct.status);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(START_TIME_FIELD_DESC);
      oprot.writeI64(struct.startTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(FINISH_TIME_FIELD_DESC);
      oprot.writeI64(struct.finishTime);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class JobInfoTupleSchemeFactory implements SchemeFactory {
    public JobInfoTupleScheme getScheme() {
      return new JobInfoTupleScheme();
    }
  }

  private static class JobInfoTupleScheme extends TupleScheme<JobInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, JobInfo struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetJobId()) {
        optionals.set(0);
      }
      if (struct.isSetAppId()) {
        optionals.set(1);
      }
      if (struct.isSetStatus()) {
        optionals.set(2);
      }
      if (struct.isSetStartTime()) {
        optionals.set(3);
      }
      if (struct.isSetFinishTime()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetJobId()) {
        oprot.writeI32(struct.jobId);
      }
      if (struct.isSetAppId()) {
        oprot.writeString(struct.appId);
      }
      if (struct.isSetStatus()) {
        oprot.writeString(struct.status);
      }
      if (struct.isSetStartTime()) {
        oprot.writeI64(struct.startTime);
      }
      if (struct.isSetFinishTime()) {
        oprot.writeI64(struct.finishTime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, JobInfo struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.jobId = iprot.readI32();
        struct.setJobIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.appId = iprot.readString();
        struct.setAppIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.status = iprot.readString();
        struct.setStatusIsSet(true);
      }
      if (incoming.get(3)) {
        struct.startTime = iprot.readI64();
        struct.setStartTimeIsSet(true);
      }
      if (incoming.get(4)) {
        struct.finishTime = iprot.readI64();
        struct.setFinishTimeIsSet(true);
      }
    }
  }

}

