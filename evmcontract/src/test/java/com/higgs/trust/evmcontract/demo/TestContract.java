package com.higgs.trust.evmcontract.demo;

import com.higgs.trust.evmcontract.crypto.ECKey;
import com.higgs.trust.evmcontract.facade.exception.ContractExecutionException;
import com.higgs.trust.evmcontract.solidity.Abi;
import com.higgs.trust.evmcontract.solidity.SolidityType;
import com.higgs.trust.evmcontract.solidity.compiler.CompilationResult;
import com.higgs.trust.evmcontract.solidity.compiler.SolidityCompiler;
import com.higgs.trust.evmcontract.vm.DataWord;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.higgs.trust.evmcontract.solidity.compiler.SolidityCompiler.Options.*;

/**
 * @author duhongming
 * @date 2018/11/19
 */
public class TestContract {
    private static final String ERROR_SIGNATURE = "08c379a0";

    private ContractService contractService;

    @Before
    public void onBefore() {
        contractService = new ContractService(null);
    }

    @After
    public void onAfter() {
        contractService.destroy();
    }

    @Test
    public void testCreateContract() throws IOException {
//        String targetContract = "test/resources/contract/MultiSign.sol";
        String targetContract = "test/resources/contract/Token.sol";
        Path source = Paths.get("src", targetContract);

        String multiSignContrator = "MultiSign(address[],uint16,address[])";
        String tokenContrator = "Token(address,string,uint)";

        SolidityCompiler.Option allowPathsOption = new SolidityCompiler.Options.AllowPaths(Collections.singletonList(source.getParent().getParent()));
        SolidityCompiler.Result res = SolidityCompiler.compile(source.toFile(), true, ABI, BIN, INTERFACE, METADATA, allowPathsOption);
        CompilationResult result = CompilationResult.parse(res.output);
        CompilationResult.ContractMetadata metadata = result.getContract(targetContract.substring(targetContract.lastIndexOf("/") + 1, targetContract.indexOf(".")));

        List<String> allAddress = new ArrayList<>(3);
        allAddress.add("e53a3fd600be283181f7396c35da469050651be7");
        allAddress.add("4a4375e93a728c7d68c04d3a4fe89cca1ddc6e4d");
        allAddress.add("010f774849c57fcb9f73d159daa85c20e197f0d2");

        List<String> mustAddress = new ArrayList<>(2);
        mustAddress.add("e53a3fd600be283181f7396c35da469050651be7");

        String ownerAddress = "d395363c3615fb28f16fa9adb358233efea7c40f";
        String tokenSymbol = "BTC";
        BigInteger totalSupply = new BigInteger("1000000000000000");

        byte[] nonce = BigIntegers.asUnsignedByteArray(new BigInteger(Objects.toString(System.currentTimeMillis())));
        byte[] code = Abi.Constructor.of(tokenContrator, Hex.decode(metadata.bin), ownerAddress, tokenSymbol, totalSupply);
//        byte[] code = Abi.Constructor.of(multiSignContrator, Hex.decode(metadata.bin), allAddress, 2, mustAddress);
        byte[] receiveAddress = Hex.decode("");
        byte[] value = Hex.decode("");
        byte[] address = contractService.createContract(nonce, receiveAddress, value, code);
        System.out.printf("address=%s\n", Hex.toHexString(address));
        contractService.commit();
    }

    @Test
    public void testInvoke() {
//        //代币合约地址
        byte[] contractAddress = Hex.decode("9c136d4c8528058f1ecc4657293082e63bb0e8da");

        //call contract
        Abi.Function getAllParam = Abi.Function.of("(address[], address[], uint) getAllParam()");
        Abi.Function balanceOf = Abi.Function.of("(uint, uint) balanceOf(address)");
        Abi.Function verifyMultiSign = Abi.Function.of("(bool) verifyMultiSign(bytes,bytes32)");
        Abi.Function transfer = Abi.Function.of("(bool) transfer(address,uint,bool,bytes,address)");

        String priKey = "191a31158f6d44d2956a5b406cff3ba193852ecf50abfd55921a6ad879d67fc7";
        String fromAddress = "d395363c3615fb28f16fa9adb358233efea7c40f";
        fromAddress = "e346b924df879eecba75ed47b3f972513e452ec8";
        String toAddress = "d395363c3615fb28f16fa9adb358233efea7c40f";
//        toAddress = "e346b924df879eecba75ed47b3f972513e452ec8";
        BigInteger amount = new BigInteger("1000000000000");
        amount = new BigInteger("100");
//        byte[] signedStrs = getSinature(priKey, contractAddress, fromAddress, toAddress, amount);
        byte[] signedMultiStrs = multiSignTest(contractAddress, fromAddress, toAddress, amount);
        String multiSignContractAddr = "74f903fd6b7b805d3064a993a5b9626a77497269";

        //参数编码
        boolean multiSignFlag = false;
//        multiSignFlag = true;
//        byte[] transferEncode = transfer.encode(toAddress, amount,multiSignFlag, null, multiSignContractAddr);
        byte[] verfiyMultiEncode = verifyMultiSign.encode(signedMultiStrs, getSourceHash(contractAddress, fromAddress, toAddress, amount));
        //合约调用
        runContractTest(Hex.decode(multiSignContractAddr), verifyMultiSign, verfiyMultiEncode);
//        runContractTest(Hex.decode(multiSignContractAddr),getAllParam,getAllParam.encode());

        //查余额
//        balanceOf(contractAddress, toAddress);
    }

    @Test
    public void OneTest() {
        DataWord one = DataWord.of((byte) 1);
        System.out.println(one);
    }

    public void balanceOf(byte[] contractAddress, String address) {
        Abi.Function func = Abi.Function.of("(uint, uint) balanceOf(address)");
        runContractTest(contractAddress, func, func.encode(address));
    }

    public void runContractTest(byte[] contractAddress, Abi.Function fun, byte[] paramData) {
        byte[] value = Hex.decode("");
        byte[] nonce = BigIntegers.asUnsignedByteArray(BigInteger.ZERO);
        byte[] resultData = contractService.invokeContract(nonce, contractAddress, value, paramData);
        if (Hex.toHexString(resultData).startsWith(ERROR_SIGNATURE)) {
            String errorMessage = parseRevertInformation(resultData);
            System.out.println(errorMessage);
        }
        List<?> result = fun.decodeResult(resultData);
        System.out.println();
        System.out.println(result);
        contractService.commit();
    }

    private byte[] getSourceHash(byte[] contractAddress, String from, String to, BigInteger amount) {
        byte[] value = Hex.decode("");
        byte[] nonce = BigIntegers.asUnsignedByteArray(BigInteger.ZERO);
        Abi.Function func = Abi.Function.of("(bytes32) getSourceHash(address,address,uint)");
        byte[] resultData = contractService.invokeContract(nonce, contractAddress, value, func.encode(from, to, amount));
        List<?> result = func.decodeResult(resultData);
        return (byte[]) result.get(0);
    }

    public byte[] getSinature(String priKey, byte[] contractAddress, String fromAddr, String toAddr, BigInteger value) {
        ECKey ecKey = ECKey.fromPrivate(Hex.decode(priKey));
        byte[] sourceHash = getSourceHash(contractAddress, fromAddr, toAddr, value);
//        System.out.println(Hex.toHexString(sourceHash));
        byte[] signedStrs = ecKey.sign(sourceHash).toByteArray();
//        System.out.println(Hex.toHexString(signedStrs));
        return signedStrs;
    }

    public byte[] multiSignTest(byte[] contractAddr, String from, String to, BigInteger value) {
        String[] pris = {
                "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7",
                "3e465c393aebac5b5d4e0c770179723db57a41b7a3e5e75dd98cbc009174135e",
                "3da393045ba96c4ca49479663c327278634b603497b850af716a7bcbaad1a098"
        };
        StringBuilder builder = new StringBuilder();
//        byte[] bytes1 = null;
        for (int i = 0; i < pris.length; i++) {
            byte[] bytes1 = getSinature(pris[i], contractAddr, from, to, value);
//            if (i > 0) {
//                bytes1[0] = bytes1[2];
//            }
//            if (i == 2) {
//                byte[] bytes = new byte[bytes1.length + 1];
//                System.arraycopy(bytes1,0,bytes,0,bytes1.length);
//                bytes[bytes.length-1] = 0;
//                bytes1 = bytes;
//            }
            builder.append(Hex.toHexString(bytes1));
        }
        System.out.println(builder.toString());
        return Hex.decode(builder.toString());
    }

    public static void main(String[] args) {
        List<Abi.Entry.Param> inputs = new ArrayList<>();
        List<Abi.Entry.Param> outputs = new ArrayList<>();
        Abi.Entry.Param p1 = new Abi.Entry.Param();
        p1.type = new SolidityType.IntType("uint256");
        Abi.Entry.Param p2 = new Abi.Entry.Param();
        p2.type = new SolidityType.IntType("uint256");
        inputs.add(p1);
        inputs.add(p2);

        Abi.Function func = new Abi.Function(false, "putVal", inputs, outputs, false);
        byte[] sign = func.encode();
        func.encode();


        System.out.println(Hex.toHexString(sign));
        System.out.println(Hex.toHexString(Abi.Function.of("putVal(uint256,uint256)").encode(1, 2)));
    }

    private String parseRevertInformation(byte[] hReturn) {
        // In solidity code, revert has no message.
        if (ArrayUtils.isEmpty(hReturn)) {
            return "";
        }

        // According to solidity design, revert message must start
        // with "08c379a0", which is the abi coding for method
        // signature "(string) error(string)".
        if (!Hex.toHexString(hReturn).startsWith(ERROR_SIGNATURE)) {
            throw new ContractExecutionException("return is not a standard revert message");
        }

        byte[] abiData = Arrays.copyOfRange(hReturn, ERROR_SIGNATURE.length() / 2, hReturn.length);
        Abi.Function function = Abi.Function.of("(string) error(string)");
        List<?> list = function.decodeResult(abiData, false);

        if (list.size() != 1 || !(list.get(0) instanceof String)) {
            throw new ContractExecutionException("parsing revert message fail");
        }

        return (String) list.get(0);
    }
}
