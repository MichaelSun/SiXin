package com.renren.mobile.chat.contact;

import java.util.ArrayList;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.util.CryptUtil;
import com.renren.mobile.chat.RenrenChatApplication;

/**
 * 对一条通讯录记录的数据建模
 * @author zhenning.yang
 *
 */
public class Contact implements Parcelable {

	/** 无效序列号 */
	public static final int INVALID_SERIAL_NUMBER = -1;
	
	/** 序列号 */
	long serialNumber = INVALID_SERIAL_NUMBER;
	
	/** renren uid, 不从通讯录读取 只写入*/
	long uid;
	
	/** 名 */
	String firstName;

	/** 姓 */
	String lastName;
	
	/** 全名 */
	String fullName;
	
	/** 电话列表 */
	ArrayList<Phone> phones = new ArrayList<Phone>();
	
	/** 电邮列表 */
	ArrayList<EMail> emails = new ArrayList<EMail>();
	
	/**	网址 不从本地通讯录读取 只写入*/
	String webSite;
	
	/** 头像地址 不从本地通讯录读取 只写入 */
	public String headUrl;
	
	/** 头像byte数组*/
	public byte[] headphoto;
	
	int contactOperation;
	
	public boolean needUpdateHeadPhoto;
	public long rawContactId;
	
	
	public static final int CONTACT_OPERATION_UPDATE_HEAD_PHOTO = 1;
	public static final int CONTACT_OPERATION_RECOMMAND_FRIEND = 2;
	public static final int CONTACT_OPERATION_QUASIFRIEND = 3;
	public static final int CONTACT_OPERATION_INSERT = 4;
	
	public Contact () {
		
	}
	
	public long getSerialNumber() {
		return serialNumber;
	}

	public Contact setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public Contact setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Contact setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public Contact setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public Contact setName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		return this;
	}
	
	public Contact addPhone(String number, String label, int phoneType) {
		this.phones.add(new Phone(number, label, phoneType));
		return this;
	}
	
	public ArrayList<Phone> getPhones() {
		return this.phones;
	}
	
	public Contact addEMail(String number, String label, int emailType) {
		this.emails.add(new EMail(number, label, emailType));
		return this;
	}
	
	public ArrayList<EMail> getEMails() {
		return this.emails;
	}
	
	public String getWebSite () {
		return webSite;
	}
	
	public Contact setWebSite (String webSite) {
		this.webSite = webSite;
		return this;
	}
	
	public long getUid () {
		return uid;
	}
	
	public Contact setUid (long uid) {
		this.uid = uid;
		return this;
	}
	
	public Contact setHeadUrl (String headUrl) {
		this.headUrl = headUrl;
		return this;
	}
	
	public String getHeadUrl () {
		return this.headUrl;
	}
	
	public Contact setContactOperation (int contactOperation) {
		this.contactOperation = contactOperation;
		return this;
	}
	
	public int getContactOperation () {
		return this.contactOperation;
	}
	
	public Contact setHeadPhoto(byte[] photo){
	    this.headphoto = photo;
	    return this;
	}
	public byte[] getHeadPhoto(){
	    return this.headphoto;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(serialNumber).append(' ');
		sb.append(this.firstName).append(' ').append(this.lastName).append(' ').append(this.fullName);
		for (Phone phone : this.phones) {
			sb.append(' ').append(phone);
		}
		for (EMail email : this.emails) {
			sb.append(' ').append(email);
		}
		sb.append(" headUrl : " + headUrl);
		
		return sb.toString();
	}
	
	/** 
	 * 对一条电话记录的数据建模。
	 */
	public static class Phone implements Parcelable {
		
		public final static String LABEL_MOBILE = "mobile"; // TODO: use Android string res.
		public final static String LABEL_HOME = "home";
		public final static String LABEL_WORK = "work";
		public final static String LABEL_OTHER = "other";
		
		/** 电话号码 */
		String number;
		
		/** 电话标签 */
		String label;
		
		/** type */
		int type;
		
		public Phone(String number, String label, int type) {
			this.number = number;
			this.label = label;
			this.type = type;
		}
		
		public String getNumber() {
			return this.number;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getType () {
			return this.type;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(number);
			dest.writeString(label);
			dest.writeInt(type);
			
		}

		public Phone(Parcel in) {
			number = in.readString();
			label = in.readString();
			type = in.readInt();
		}

		public static final Parcelable.Creator<Phone> CREATOR = new Parcelable.Creator<Phone>() {
			public Phone createFromParcel(Parcel in) {
				return new Phone(in);
			}

			public Phone[] newArray(int size) {
				return new Phone[size];
			}
		};

		@Override
		public int describeContents() {
			return this.hashCode();
		}
		
		@Override
		public String toString() {
			return this.number + ' ' + this.label;
		}
	}
	
	/** 
	 * 对一条电邮记录的数据建模。
	 */
	public static class EMail implements Parcelable {
		
		public final static String LABEL_MOBILE = "mobile";
		public final static String LABEL_HOME = "home";
		public final static String LABEL_WORK = "work";
		public final static String LABEL_OTHER = "other";
		
		/** 电邮地址 */
		String address;
		
		/** 电邮标签 */
		String label;
		
		/** type */
		int type;
		
		public EMail(String address, String label, int type) {
			this.address = address;
			this.label = label;
			this.type = type;
		}
		
		public String getAddress() {
			return this.address;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getType () {
			return this.type;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(address);
			dest.writeString(label);
			dest.writeInt(type);
			
		}
		
		public EMail(Parcel in) {
			address = in.readString();
			label = in.readString();
			type = in.readInt();
		}

		public static final Parcelable.Creator<EMail> CREATOR = new Parcelable.Creator<EMail>() {
			public EMail createFromParcel(Parcel in) {
				return new EMail(in);
			}

			public EMail[] newArray(int size) {
				return new EMail[size];
			}
		};

		@Override
		public int describeContents() {
			return this.hashCode();
		}
		
		@Override
		public String toString() {
			return this.address + ' ' + this.label;
		}
	}
	
	public static ArrayList<Contact> parseContacts (JsonObject obj) {
		
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		if(obj != null) {
			
			JsonArray updateArray = obj.getJsonArray("update_list");
			parseArray(updateArray, contacts, CONTACT_OPERATION_UPDATE_HEAD_PHOTO);
			
			JsonArray recommandArray = obj.getJsonArray("recommend_list");
			parseArray(recommandArray, contacts, CONTACT_OPERATION_RECOMMAND_FRIEND);
			
			JsonArray quasiFriendArray = obj.getJsonArray("quasifriend_list");
			parseArray(quasiFriendArray, contacts, CONTACT_OPERATION_QUASIFRIEND);
			
			JsonArray insertArray = obj.getJsonArray("renren_contact_list");
			parseArray(insertArray, contacts, CONTACT_OPERATION_INSERT);
			
		}
		
		return contacts;
		
		
	}
	
	private static void parseArray (JsonArray array, ArrayList<Contact> contacts, int contactOperation) {
		if(array != null) {
			JsonObject[] objs = new JsonObject[array.size()];
			array.copyInto(objs);
			for(JsonObject obj : objs) {
				Contact contact = parseContact(obj);
				contact.setContactOperation(contactOperation);
				if (contact != null) {
					contacts.add(contact);
				}
			}
		}
	}
	
	public static Contact parseContact (JsonObject obj) {
		Contact contact = null;
		if (obj != null) {
			contact = new Contact();
			contact.serialNumber = obj.getNum("serial_number");
			contact.headUrl = obj.getString("head_url");
			contact.webSite = obj.getString("profile_url");
			contact.uid = obj.getNum("user_id");
			contact.fullName = obj.getString("user_name");
			String phone = obj.getString("phone_number");
			if(!TextUtils.isEmpty(phone)) {
				contact.addPhone(phone, null, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
			}
		}
		return contact;
	}
	
	/**
	 * 合并网络返回的联系人
	 * @param contact 接口返回的联系人数据
	 * @return
	 */
	public Contact joinContact (Contact contact) {
		if(contact.serialNumber == this.serialNumber) {
			this.webSite = contact.webSite;
			this.uid = contact.uid;
			this.headUrl = contact.headUrl;
		}
		return this;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(serialNumber);
		dest.writeLong(uid);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(fullName);
		dest.writeArray(phones.toArray());
		dest.writeArray(emails.toArray());
		dest.writeString(webSite);
		dest.writeString(headUrl);
		dest.writeInt(contactOperation);
	}

	
	@SuppressWarnings("unchecked")
	public Contact(Parcel in) {
		serialNumber = in.readLong();
		uid = in.readLong();
		firstName = in.readString();
		lastName = in.readString();
		fullName = in.readString();
		phones = in.readArrayList(RenrenChatApplication.mContext.getClassLoader());
		emails = in.readArrayList(RenrenChatApplication.mContext.getClassLoader());
		webSite = in.readString();
		headUrl = in.readString();
		contactOperation = in.readInt();
	}

	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
		public Contact createFromParcel(Parcel in) {
			return new Contact(in);
		}

		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};

	@Override
	public int describeContents() {
		return (int) serialNumber;
	}
	
	/**
	 * 生成通讯录条目组的JSON格式字符串。
	 * 
	 * @param contacts
	 *            通讯录数据组
	 * @return 通讯录数据组的JSON格式字符串
	 */
	public static String generateContactsJsonString(Contact[] contacts, String sessionKey) {

		JsonObject juploadContact = new JsonObject();

		String mobileType = Build.MODEL;
		juploadContact.put("mobile_type", mobileType);

		JsonArray jcontacts = new JsonArray();
		for (Contact contact : contacts) {
			JsonObject jcontact = generateContactJson(contact);
			jcontacts.add(jcontact);
		}
		juploadContact.put("contacts", jcontacts);
		String unEnCryptedString = juploadContact.toJsonString();
		return CryptUtil.encryptString(unEnCryptedString, sessionKey);
	}

	/**
	 * 生成一条通讯录条目的JSON数据。
	 * 
	 * @param contact
	 *            一条通讯录记录。
	 * @return 一条通讯录记录的JSON数据。
	 * 
	 *         TODO 这个方法需要移到Contact类中
	 */
	public static JsonObject generateContactJson(Contact contact) {
		JsonObject jcontact = new JsonObject();

		long serialNumber = contact.getSerialNumber();
		jcontact.put("serial_number", String.valueOf(serialNumber));

		String fullName = contact.getFullName();
		if (!TextUtils.isEmpty(fullName)) {
			jcontact.put("full_name", fullName);
		} else {
			String firstName = contact.getFirstName();
			jcontact.put("first_name", firstName);
			String familyName = contact.getLastName();
			jcontact.put("last_name", familyName);
		}

		JsonArray jphones = new JsonArray();
		ArrayList<Contact.Phone> phones = contact.getPhones();
		for (Contact.Phone phone : phones) {
			JsonObject jphone = new JsonObject();
			String phoneNumber = phone.getNumber();
			jphone.put("phone_number", phoneNumber);
			String phoneLabel = phone.getLabel();
			jphone.put("phone_label", phoneLabel);
			jphones.add(jphone);
		}
		if (jphones.size() > 0) {
			jcontact.put("phones", jphones);
		}

		ArrayList<Contact.EMail> emails = contact.getEMails();
		JsonArray jemails = new JsonArray();

		for (Contact.EMail email : emails) {
			JsonObject jemail = new JsonObject();
			String emailAddress = email.getAddress();
			jemail.put("email", emailAddress);
			String emailLabel = email.getLabel();
			jemail.put("email_label", emailLabel);
			jemails.add(jemail);
		}

		if (jemails.size() > 0) {
			jcontact.put("emails", jemails);
		}
		return jcontact;
	}
}
