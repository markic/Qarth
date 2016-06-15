/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Marin
 */
public class UnitOfWork {
    private EntityManagerFactory _factory = null;
    private EntityManager _currentSession = null;
    
    public UnitOfWork(){
        _factory = Persistence.createEntityManagerFactory("QarthPU");
        _currentSession = _factory.createEntityManager();
    }
    
    protected EntityManager GetCurrentSession(){
        return _currentSession;
    }
    
    public <T> List<T> GetResults(String query){
        _currentSession.getTransaction().begin();
        List<T> result = GetCurrentSession().createQuery(query).getResultList();
        _currentSession.getTransaction().commit();
        return result;
    }
    
    public <T> List<T> GetResults(String query, Object [] parameters){
        _currentSession.getTransaction().begin();
        Query q = GetCurrentSession().createQuery(query);
        for (int  i = 0; i < parameters.length; i++){
            q = q.setParameter(i + 1, parameters[i]);
        }

        List<T> result = q.getResultList();
        _currentSession.getTransaction().commit();
        return result;
    }
    
    public Object PersistObject(Object entity){
        _currentSession.getTransaction().begin();
        GetCurrentSession().persist(entity);
        _currentSession.getTransaction().commit();
        return entity;
    }
    
    public void close(){
        _currentSession.close();
        _factory.close();
    }  
}
