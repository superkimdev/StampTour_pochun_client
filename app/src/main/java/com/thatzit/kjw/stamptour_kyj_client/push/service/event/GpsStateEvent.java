package com.thatzit.kjw.stamptour_kyj_client.push.service.event;
/**
 * �̺�Ʈ Ŭ������ ������� state�� gps ������¿��� true, �񿬰���¿��� false�� ��ȴ�.
 * state���� isState()�޼��� ȣ��� �����´�.
 * 
 */
public class GpsStateEvent {
	
	private boolean state;
	/**
	 * <pre>
	 * GpsStateEvent
	 * CheckGPSIsRunningUtill.onCheckGPSIsRunning()�� ��ȯ������
	 * ���ϸ��
	 * </pre>
	 * @param state
	 */
	public GpsStateEvent(boolean state) {
		super();
		this.state = state;
	}
	/**
	 * state���� ������ ���� �Լ� state�� Gps ������¿� ��� ����� true �񿬰�� false�̴�.
	 * @param ����
	 * @return true or false
	 */
	public boolean isState() {
		return state;
	}
}
