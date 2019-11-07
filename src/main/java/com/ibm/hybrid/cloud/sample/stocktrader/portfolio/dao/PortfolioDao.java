/*
       Copyright 2019 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ibm.hybrid.cloud.sample.stocktrader.portfolio.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.hybrid.cloud.sample.stocktrader.portfolio.json.Portfolio;

public class PortfolioDao {
    
    private EntityManager em = null;
    private static EntityManagerFactory emFactoryObj;

    static {
           emFactoryObj = createEntityManagerFactory();
    }
       
    public static EntityManagerFactory createEntityManagerFactory() {
        String JDBC_URL = 
               "jdbc:db2://" + System.getenv("JDBC_HOST") + ":" + 
               System.getenv("JDBC_PORT") + "/" + System.getenv("JDBC_DB");
        Map properties = new HashMap();
        properties.put("javax.persistence.jdbc.driver", "com.ibm.db2.jcc.DB2Driver");
        properties.put("javax.persistence.jdbc.url", JDBC_URL);
        properties.put("javax.persistence.jdbc.user", System.getenv("JDBC_ID"));
        properties.put("javax.persistence.jdbc.password", System.getenv("JDBC_PASSWORD"));
        return Persistence.createEntityManagerFactory("jpa-unit", properties);           
    }
  
    // This Method Is Used To Retrieve The 'EntityManager' Object
    public EntityManager getEntityManager() {
        if (em == null) {
              if (emFactoryObj == null)
                     emFactoryObj = emFactoryObj = createEntityManagerFactory();
              em = emFactoryObj.createEntityManager();
        }
        return em ;
    }

    public void createPortfolio(Portfolio portfolio) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(portfolio);
        em.getTransaction().commit();         
    }

    public Portfolio readEvent(String owner) {
        EntityManager em = getEntityManager();
        return em.find(Portfolio.class, owner);
    }

    public void updatePortfolio(Portfolio portfolio) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(portfolio);
        em.flush();
        em.getTransaction().commit();
    }

    public void deletePortfolio(Portfolio portfolio) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(portfolio));
        em.getTransaction().commit();
    }

    public List<Portfolio> readAllPortfolios() {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("Portfolio.findAll", Portfolio.class).getResultList();
    }

}
