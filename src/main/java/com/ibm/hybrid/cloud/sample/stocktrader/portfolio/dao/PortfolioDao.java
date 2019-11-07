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

import javax.annotation.Resource;

import javax.enterprise.context.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;

import com.ibm.hybrid.cloud.sample.stocktrader.portfolio.json.Portfolio;

@RequestScoped
public class PortfolioDao {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Resource
    private UserTransaction utx;
    
    public void createPortfolio(Portfolio portfolio) {
        em.persist(portfolio);
    }

    public Portfolio readEvent(String owner) {
        return em.find(Portfolio.class, owner);
    }

    public void updatePortfolio(Portfolio portfolio) {
        try {
              utx.begin();
              em.merge(portfolio);
              em.flush();
              utx.commit();
        } catch (Exception ex) {
              try {
                     System.out.println("Update failed: "+ex.getMessage());
                     utx.rollback();
              } catch (Exception exe) {
                     System.out.println("Rollback failed: "+exe.getMessage());
              }
        }
    }

    public void deletePortfolio(Portfolio portfolio) {
        em.remove(em.merge(portfolio));
    }

    public List<Portfolio> readAllPortfolios() {
        return em.createNamedQuery("Portfolio.findAll", Portfolio.class).getResultList();
    }

}
