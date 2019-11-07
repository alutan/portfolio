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

//import javax.annotation.Resource;

//import javax.enterprise.context.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import javax.persistence.PersistenceContext;
//import javax.transaction.UserTransaction;
import java.util.List;

import com.ibm.hybrid.cloud.sample.stocktrader.portfolio.json.Stock;

//@RequestScoped
public class StockDao {

//    @PersistenceContext(name = "jpa-unit")
//    private EntityManager em;

//    @Resource
//    private UserTransaction utx;
 
    private EntityManager em = null;
    private static final EntityManagerFactory emFactoryObj;
 
    static {
        emFactoryObj = Persistence.createEntityManagerFactory("jpa-unit");
    }
 
    // This Method Is Used To Retrieve The 'EntityManager' Object
    public EntityManager getEntityManager() {
        if (em == null) {
              if (emFactoryObj == null)
                     emFactoryObj = Persistence.createEntityManagerFactory("jpa-unit");
              em = emFactoryObj.createEntityManager();
        }
        return em ;
    }
       
    public void createStock(Stock stock) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(stock);
        em.getTransaction().commit();
    }

    public Stock readEvent(String symbol) {
        EntityManager em = getEntityManager();
        return em.find(Stock.class, symbol);
    }

    public void updateStock(Stock stock) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
//        try {
//              utx.begin();
        em.merge(stock);
        em.flush();
//              utx.commit();
//        } catch (Exception ex) {
//              try {
//                     System.out.println("Update failed: "+ex.getMessage());
//                     utx.rollback();
//              } catch (Exception exe) {
//                     System.out.println("Rollback failed: "+exe.getMessage());
//              }
//        }
       em.getTransaction().commit();
    }

    public void deleteStock(Stock stock) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge((stock)));
        em.getTransaction().commit();
    }

    public void detachStock(Stock stock) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.detach(stock);
        em.getTransaction().commit();
    }

    public List<Stock> readStockByOwner(String owner) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("Stock.findByOwner", Stock.class)
            .setParameter("owner", owner).getResultList();
    }
    public List<Stock> readStockByOwnerAndSymbol(String owner, String symbol) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("Stock.findByOwnerAndSymbol", Stock.class)
            .setParameter("owner", owner)
            .setParameter("symbol", symbol).getResultList();
    }
}
