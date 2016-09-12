package com.thatzit.kjw.stamptour_kyj_client.push.service.event;

import android.location.Location;
/**
 * <pre>
 * �̺�Ʈ Ŭ������ gps������¿����� ���̵ȴ�.
 * ������� location�� Location��ü�� ���浵���� �������ִ�.
 * location���� getLocation()�޼��带 �̿��Ѵ�.
 * </pre>
 */
public class LocationEvent {
	private Location location;
	/**
	 * <pre>
	 * LocationEvent���ڷ� �̺�Ʈ�߻�� ���� �� 
	 * Location��ü�� �Ķ���ͷ� ����Ѵ�.
	 * </pre>
	 * @param location
	 */
	public LocationEvent(Location location) {
		super();
		this.location = location;
	}
	/**
	 * <pre>
	 * ������� Location Ÿ�� location�� ��ȯ�ϴ� getter
	 * </pre> 
	 * @return Location
	 */
	public Location getLocation() {
		return location;
	}
	
}
