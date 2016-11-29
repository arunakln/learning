using System;
using Aruna_technicalTest.Models;
using Aruna_technicalTest.DAL;
using Aruna_technicalTest;
using System.Data.Entity;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Mock InvoiceContext for Unit testing
 */

namespace Aruna_technicalTest_UnitTest
{
    class TestInvoiceContext : IInvoiceContext
    {
        public TestInvoiceContext()
    {
        this.InvoiceList = new TestInvoiceDbSet();
    }

    public DbSet<InvoiceModel> InvoiceList { get; set; }

    public int SaveChanges()
    {
        return 0;
    }

    public void MarkAsModified(InvoiceModel item) { }
    public void Dispose() { }

    }
}
