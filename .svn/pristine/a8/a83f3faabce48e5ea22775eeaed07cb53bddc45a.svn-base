package com.common.contactscontract;

import java.util.Hashtable;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.core.util.AbstractApplication;

public class ContactsContractMethods {

	private static ContactsContractMethods sInstance = new ContactsContractMethods();
	
	public static ContactsContractMethods getInstance(){
		return sInstance;
	}
	
	/* 查询Data表时的投影 */
	private final static String[] PROJECTION = new String[] {
		// Metadata
		ContactsContract.Data.RAW_CONTACT_ID, 
		ContactsContract.Data.MIMETYPE,

		// Name
		ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, 
		ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
		
		// DISPLAY_NAME由FAMILY_NAME，GIVEN_NAME拼装而来，对用户没有UI接口
		ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,

		// photo byte[]
		ContactsContract.CommonDataKinds.Photo.PHOTO,
		
		// Phone
		ContactsContract.CommonDataKinds.Phone.NUMBER, 
		ContactsContract.CommonDataKinds.Phone.TYPE, 
		ContactsContract.CommonDataKinds.Phone.LABEL,

		// EMail
		ContactsContract.CommonDataKinds.Email.DATA, 
		ContactsContract.CommonDataKinds.Email.TYPE, 
		ContactsContract.CommonDataKinds.Email.LABEL,

		ContactsContract.Data.CONTACT_ID };

	/* 查询Data表时的选择 */
	private final static String SELECTION = ContactsContract.Data.MIMETYPE
			+ "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
			+ "'" + " OR " + ContactsContract.Data.MIMETYPE + "='"
			+ ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
			+ "'" + " OR " + ContactsContract.Data.MIMETYPE + "='"
			+ ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'"
			+ " OR " + ContactsContract.Data.MIMETYPE + "='"
			+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
	
	/**
	 * 读取通讯录中一个人的记录
	 * 
	 * @return
	 */
	public ContactsContractModel[] loadContact(long contactId) {

		final String[] projection = PROJECTION;
		final String selection = ContactsContract.Data.CONTACT_ID + "=?";
		final String[] selectionArgs = new String[] { String.valueOf(contactId) };

		ContentResolver cr = AbstractApplication.getAppContext().getContentResolver();
		ContactsContractModel[] c = new ContactsContractModel[0];
		Cursor cursor = null;
		try {
			cursor = cr.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);
			if (cursor != null) {
				c = loadContacts(cursor);
			}
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception e) {
				}
			}
		}
		return c;
	}
	
	/**
	 * 读出通讯录中的所有记录。
	 * 
	 * @return 通讯录中包含的所有记录。
	 */
	public ContactsContractModel[] loadAllContacts() {
		ContentResolver cr = AbstractApplication.getAppContext().getContentResolver();
		ContactsContractModel[] c = new ContactsContractModel[0];
		Cursor cursor = null;
		try {
			cursor = cr.query(ContactsContract.Data.CONTENT_URI, PROJECTION,
					SELECTION, null, null);
			
			if(cursor!=null){
				c = loadContacts(cursor);
			}
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception e) {
				}
			}
		}
		return c;
	}


	/**
	 * 读取cursor中的全部contact
	 * 
	 * @param cursor
	 * @return
	 */
	private ContactsContractModel[] loadContacts(Cursor cursor) {

		if (cursor == null || !cursor.moveToFirst()) {
			return new ContactsContractModel[0];
		}
		Hashtable<Long, ContactsContractModel> table = new Hashtable<Long, ContactsContractModel>();

		do {

			int index = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
			String mime = cursor.getString(index);

			index = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
			long rawContactId = cursor.getLong(index);

			// Name
			if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE.equals(mime)) {

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
				String displayName = cursor.getString(index);

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
				String familyName = cursor.getString(index);

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
				String givenName = cursor.getString(index);

				ContactsContractModel contact = table.get(rawContactId);
				if (contact == null) {
					contact = new ContactsContractModel();
					table.put(new Long(rawContactId), contact);

					contact.setSerialNumber(rawContactId);
				}

				contact.setLastName(familyName);
				contact.setFirstName(givenName);
				contact.setFullName(displayName);

				// Phone
			} else if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mime)) {

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
				int phoneType = cursor.getInt(index);

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				String phoneNumber = cursor.getString(index);

				ContactsContractModel contact = table.get(rawContactId);
				if (contact == null) {
					contact = new ContactsContractModel();
					table.put(new Long(rawContactId), contact);

					contact.setSerialNumber(rawContactId);
				}

				String phoneLabel = null;
				switch (phoneType) {
				case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
					phoneLabel = ContactsContractModel.Phone.LABEL_MOBILE;
					break;
				case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
					phoneLabel = ContactsContractModel.Phone.LABEL_WORK;
					break;
				case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
					phoneLabel = ContactsContractModel.Phone.LABEL_HOME;
					break;
				default:
				case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
					phoneLabel = ContactsContractModel.Phone.LABEL_OTHER;
					break;
				case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
					index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
					phoneLabel = cursor.getString(index);
					break;
				}

				contact.addPhone(phoneNumber, phoneLabel, phoneType);

			} else if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE.equals(mime)) {

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
				String email = cursor.getString(index);

				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);
				int emailType = cursor.getInt(index);

				ContactsContractModel contact = table.get(rawContactId);
				if (contact == null) {
					contact = new ContactsContractModel();
					table.put(new Long(rawContactId), contact);

					contact.setSerialNumber(rawContactId);
				}

				String emailLabel = null;
				switch (emailType) {
				case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
					emailLabel = ContactsContractModel.EMail.LABEL_MOBILE;
					break;
				case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
					emailLabel = ContactsContractModel.EMail.LABEL_HOME;
					break;
				case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
					emailLabel = ContactsContractModel.EMail.LABEL_WORK;
					break;
				default:
				case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
					emailLabel = ContactsContractModel.EMail.LABEL_OTHER;
					break;
				case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
					index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL);
					emailLabel = cursor.getString(index);
					break;
				}

				contact.addEMail(email, emailLabel, emailType);

			} else if (ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE.equals(mime)) {
				index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
				ContactsContractModel contact = table.get(rawContactId);
				if (contact == null) {
					contact = new ContactsContractModel();
					table.put(new Long(rawContactId), contact);
					contact.setSerialNumber(rawContactId);
				}
				// 非空判断 如果为空 不写入 否则 写入Contact
				if (cursor.getBlob(index) == null) {
				} else {
					contact.setHeadPhoto(cursor.getBlob(index));
				}

			}

		} while (cursor.moveToNext());

		// Collection<Contact> col = table.values();
		// Contact[] contacts = new Contact[col.size()];
		// col.toArray(contacts);
		// 以上三行可以合并为下面一行 11.11.30

		return table.values().toArray(new ContactsContractModel[0]);
	}
}
