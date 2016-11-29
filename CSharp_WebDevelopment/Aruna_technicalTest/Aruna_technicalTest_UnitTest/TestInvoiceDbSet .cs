using System;
using System.Linq;
using Aruna_technicalTest.Models;

namespace Aruna_technicalTest
{
    class TestInvoiceDbSet : TestDBSet<InvoiceModel>
    {
        public override InvoiceModel Find(params object[] keyValues)
        {
            return this.SingleOrDefault(invoice => invoice.ID == (int)keyValues.Single());
        }
    }
}
