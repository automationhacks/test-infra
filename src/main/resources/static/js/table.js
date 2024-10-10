function createTestClassesTable(testClasses) {
    let tableHtml = '<table><tr><th>Class Name</th><th>OnCall</th><th>Flow</th><th>Test Methods</th></tr>';

    testClasses.forEach(classInfo => {
        tableHtml += `<tr>
            <td>${classInfo.className}</td>
            <td>${classInfo.onCall || 'N/A'}</td>
            <td>${classInfo.flow || 'N/A'}</td>
            <td>
                <details>
                    <summary>Show ${classInfo.testMethods.length} methods</summary>
                    <ul>
                        ${classInfo.testMethods.map(method => `<li>${method.methodName} (OnCall: ${method.onCall || 'N/A'}, Flow: ${method.flow || 'N/A'}, Service: ${method.service || 'N/A'})</li>`).join('')}
                    </ul>
                </details>
            </td>
        </tr>`;
    });

    tableHtml += '</table>';
    document.getElementById('testClassesTable').innerHTML = tableHtml;
}