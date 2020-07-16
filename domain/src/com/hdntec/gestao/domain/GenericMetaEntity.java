package com.hdntec.gestao.domain;

import java.util.Date;

public interface GenericMetaEntity<T> {

	 /** 
	    * Inclui um novo status na lista 
	    * */
	public void incluirNovoStatus(T novoStatus, Date horaStatus);

	/** 
	* Remove os elementos da lista a partir de um determinado horario
	* @param horaEvento
	*/
	public void removeStatusAPartirDeHorario(Date horario);
	
	
	/**
	*  Retorna o status correspondente a um determinado horario
	*/
	public T retornaStatusHorario(Date horario);
	
	/**
	 * clonarStatus, copia objeto, inserindo na lista de estados
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 14/06/2010
	 * @see
	 * @param 
	 * @return T
	 */
	public T clonarStatus(Date horario);
	
	
    
    /**
     * ordernar
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 16/01/2011
     * @see
     * @param 
     * @return void
     */
    public void ordernar();

    /**
     * copiarStatus
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 16/01/2011
     * @see
     * @param 
     * @return T
     */
    public T copiarStatus(T oldValue);
    
    /**
     * RemoverStatus
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 16/01/2011
     * @see
     * @param 
     * @return T
     */
    public Boolean removerStatus(T status);
}
