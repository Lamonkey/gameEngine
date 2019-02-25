package csc481_project;
/**
 * reference to code from text book on page 395
 * @author Michael
 *
 */
public class TimeLine {
	long loopIteration;
	long m_timeCycles;
	double m_timeScales;
	boolean m_isPaused;
	long begin;
	static double s_cyclesPerSecond;
	public void increamnetLoop() {
		loopIteration++;
	}
	//create the timeline
	public TimeLine() {
		loopIteration = 0;
		m_timeCycles = 0;
		m_timeScales = 1;
		m_isPaused = false;
	}
	public void init() {
		begin = System.currentTimeMillis();
	}
	//get the deltatime
	public long calcDeltaTime(TimeLine timeLine) {
		long dt = m_timeCycles - timeLine.m_timeCycles;
		return dt;
	} 
	public void setPaused(boolean wantPaused) {
		m_isPaused = wantPaused;
	}
	public boolean isPaused() {
		return m_isPaused;
		
	}
  
	public void setTimeScale(long scale) {
		m_timeScales = scale;
	}
	public double getTimeScale() {
		return this.m_timeScales;
	}
	
	/**
	 * get the next target time buy tickSize
	 * @param tickSize the unite of time to do one operation
	 * @return the target time
	 */
	public double singleStep(double tickSize) {
		if (!m_isPaused) {
			{
				//add one ideal frame interval;
				//dont forget to scale it by our current time scale
				double dtScaledCycles =  tickSize *m_timeScales;
				return m_timeCycles + dtScaledCycles;
			}
		}
		return 0;
	}
	/**
	 * update the timeline accroding to the realtime
	 * @param deltaRealTime
	 */
	public void update(double deltaRealTime) {
		if(!m_isPaused) {
			double dtScaledCycles = deltaRealTime * m_timeScales;
			m_timeCycles += dtScaledCycles;
		}
	}


}
