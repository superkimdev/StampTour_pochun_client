package com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener;


import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;

/**
 * <pre>
 * LocationEvent�� �����ϴ� ������ �������̽�
 * ������ ������Ʈ�� implement �Ͽ���
 * OnReceivedEvent()�Լ� �����ϸ� �ȴ�.
 * </pre>
 */
public interface LocationEventListener {
	/**
	* ��Ƽ��Ƽ���� ���� OnReceivedEvent�� ����� ��Ƽ��Ƽ���� OnReceivedEvent����.
    * �����׸�Ʈ���� ���� �����׸�Ʈ�� ���ϴ� ��Ƽ��Ƽ�� OnReceivedEvent�Լ��� �����ϰ� ������ �Լ��ȿ��� �����׸�Ʈ�� �̺�Ʈ ����� �����ʸ� ������ �� �����׸�Ʈ���� ������ ����Ͽ� ���
    * ex)
    * <pre>
    * {@code
    *  public void OnReceivedEvent(LocationEvent event) {
		// TODO Auto-generated method stub
		Log.d("MainActivity in OnReceivedEvent", "come");
		
		location=event.getLocation();
		lat=location.getLatitude();
		lon=location.getLongitude();
		Log.d("MainActivity in OnReceivedEvent", "lat:"+lat+"\nlon:"+lon);
		
		locationsendEvent=new MaintoFragEvent(location);
		if(locationsender!=null)locationsender.OnReceivedEvent(locationsendEvent);
		
	}}
	</pre>
	**/
	public void OnReceivedEvent(LocationEvent event);
}
