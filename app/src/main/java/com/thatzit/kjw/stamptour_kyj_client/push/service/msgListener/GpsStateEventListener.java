package com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener;

import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;

/**
 * <pre>
 * GpsStateEvent�� �����ϴ� ������ �������̽�
 * ������ ������Ʈ�� implement �Ͽ���
 * OnReceivedStateEvent()�Լ� �����ϸ� �ȴ�.
 * </pre>
 */
public interface GpsStateEventListener {
	
	/**
     * gps�����̼��� ���� Activity�� �޴´�
     * �������� �������
     * 
     */
	/**
	* ��Ƽ��Ƽ���� ���� OnReceivedStateEvent�� ����� ��Ƽ��Ƽ���� OnReceivedStateEvent����.
    * �����׸�Ʈ���� ���� �����׸�Ʈ�� ���ϴ� ��Ƽ��Ƽ�� OnReceivedStateEvent�Լ��� �����ϰ� ������ �Լ��ȿ��� �����׸�Ʈ�� �̺�Ʈ ����� �����ʸ� ������ �� �����׸�Ʈ���� ������ ����Ͽ� ���
    * ex)
    * <pre>
    * {@code
    *  public void OnReceivedStateEvent(GpsStateEvent event) {
		// TODO Auto-generated method stub
		gpssendEvent=new MaintoFragGpsStateEvent(event.isState());
		if(gpssender!=null)gpssender.OnReceivedGpsStateEvent(gpssendEvent);
	}}
	</pre>
	@param
	**/
	public void OnReceivedStateEvent(GpsStateEvent event);
}
